package com.metamapa.Config;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitingFilter extends OncePerRequestFilter {

    @Value("${rate.limit.max-requests}")
    private int MAX_REQUESTS;

    @Value("${rate.limit.window-seconds}")
    private int WINDOW_SECONDS;

    @Value("${rate.limit.block-duration-seconds}")
    private int BLOCK_DURATION;

    private final Map<String, RequestCounter> requestsPorIp = new ConcurrentHashMap<>();
    private final Map<String, Instant> blockedIps = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String clientIp = getClientIp(request);

        // verificar si ip esta bloqueada temporalmente
        if (isIpBlocked(clientIp)) {
            respondWithTooManyRequests(response, clientIp, "IP bloqueada temporalmente por exceder límite de solicitudes");
            return;
        }

        // obtener o crear info de rrl para esta ip
        RequestCounter rateLimitInfo = requestsPorIp.computeIfAbsent(clientIp, k -> new RequestCounter(WINDOW_SECONDS));

        // verificar si se excedio el límite
        if (!rateLimitInfo.allowRequest(MAX_REQUESTS)) {
            // bloquear ip temporalmente
            blockedIps.put(clientIp, Instant.now().plusSeconds(BLOCK_DURATION));
            respondWithTooManyRequests(response, clientIp, "Límite de solicitudes excedido");
            return;
        }

        // agregar headers de rl en la respuesta
        addRateLimitHeaders(response, rateLimitInfo);

        filterChain.doFilter(request, response);
/*
        String ip = obtenerIp(request);
        long now = System.currentTimeMillis();

        RequestCounter counter = requestsPorIp.get(ip);

        if (counter == null || now - counter.windowStart > WINDOW_MS) {
            requestsPorIp.put(ip, new RequestCounter(now));
        } else {
            counter.count++;
            if (counter.count > MAX_REQUESTS) {
                response.setStatus(429);
                response.getWriter().write("Rate limit excedido");
                return;
            }
        }

        filterChain.doFilter(request, response);
        */
    }

    private void addRateLimitHeaders(HttpServletResponse response, RequestCounter info) {
        response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, MAX_REQUESTS - info.getRequestCount())));
        response.setHeader("X-RateLimit-Reset", String.valueOf(info.getWindowResetTime()));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp.trim();
        }

        return request.getRemoteAddr();
    }

    private boolean isIpBlocked(String clientIp) {
        Instant blockedUntil = blockedIps.get(clientIp);
        if (blockedUntil == null) {
            return false;
        }

        if (Instant.now().isAfter(blockedUntil)) {
            // ll bloqueo expiro, eliminar de l lista
            blockedIps.remove(clientIp);
            requestsPorIp.remove(clientIp); // Resetear contador
            return false;
        }

        return true;
    }

    private void respondWithTooManyRequests(HttpServletResponse response, String clientIp, String reason) throws IOException {
        response.setStatus(429); // aca seteo el Too Many Requests
        response.setContentType("application/json");
        response.setHeader("Retry-After", String.valueOf(BLOCK_DURATION));

        Instant blockedUntil = blockedIps.get(clientIp);
        long retryAfterSeconds = blockedUntil != null
                ? Math.max(0, blockedUntil.getEpochSecond() - Instant.now().getEpochSecond())
                : BLOCK_DURATION;

        response.getWriter().write(String.format(
                "{\"error\": \"Too Many Requests\", \"message\": \"%s\", \"ip\": \"%s\", \"retryAfterSeconds\": %d}",
                reason, clientIp, retryAfterSeconds
        ));
    }

    private static class RequestCounter {
        private final int windowSizeSeconds;
        private final java.util.Deque<Long> requestTimestamps = new java.util.concurrent.ConcurrentLinkedDeque<>();
        private volatile long lastRequestTime;

        public RequestCounter(int windowSizeSeconds) {
            this.windowSizeSeconds = windowSizeSeconds;
            this.lastRequestTime = System.currentTimeMillis();
        }

        public synchronized boolean allowRequest(int maxRequests) {
            long now = System.currentTimeMillis();
            long windowStart = now - (windowSizeSeconds * 1000L);

            // eliminar timestamps fuera de la ventana actual
            while (!requestTimestamps.isEmpty() && requestTimestamps.peekFirst() < windowStart) {
                requestTimestamps.pollFirst();
            }

            // verificar si se puede aceptar la solicitud
            if (requestTimestamps.size() >= maxRequests) {
                return false;
            }

            // registrar la solicitud
            requestTimestamps.addLast(now);
            lastRequestTime = now;
            return true;
        }

        public int getRequestCount() {
            long now = System.currentTimeMillis();
            long windowStart = now - (windowSizeSeconds * 1000L);
            return (int) requestTimestamps.stream().filter(ts -> ts >= windowStart).count();
        }

        public long getWindowResetTime() {
            if (requestTimestamps.isEmpty()) {
                return System.currentTimeMillis() / 1000;
            }
            return (requestTimestamps.peekFirst() + (windowSizeSeconds * 1000L)) / 1000;
        }

        public long getLastRequestTime() {
            return lastRequestTime;
        }
    }
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("maxRequestsPerWindow", MAX_REQUESTS);
        stats.put("windowSizeSeconds", WINDOW_SECONDS);
        stats.put("blockDurationSeconds", BLOCK_DURATION);
        stats.put("activeIpsTracked", requestsPorIp.size());
        stats.put("blockedIpsCount", blockedIps.size());
        return stats;
    }

    public Map<String, Object> getIpInfo(String ip) {
        Map<String, Object> info = new java.util.HashMap<>();
        info.put("ip", ip);
        info.put("blocked", isIpBlocked(ip));

        RequestCounter rateLimitInfo = requestsPorIp.get(ip);
        if (rateLimitInfo != null) {
            info.put("currentRequests", rateLimitInfo.getRequestCount());
            info.put("remainingRequests", Math.max(0, BLOCK_DURATION - rateLimitInfo.getRequestCount()));
        } else {
            info.put("currentRequests", 0);
            info.put("remainingRequests", BLOCK_DURATION);
        }

        Instant blockedUntil = blockedIps.get(ip);
        if (blockedUntil != null) {
            info.put("blockedUntil", blockedUntil.toString());
            info.put("retryAfterSeconds", Math.max(0, blockedUntil.getEpochSecond() - Instant.now().getEpochSecond()));
        }

        return info;
    }

    public boolean unblockIp(String ip) {
        blockedIps.remove(ip);
        requestsPorIp.remove(ip);
        return true;
    }

    public boolean resetIpCounter(String ip) {
        requestsPorIp.remove(ip);
        return true;
    }
}