package com.metamapa.Config;

import com.mongodb.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mongodb.client.MongoDatabase;


@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class SchedulerConfig {
    //MEJORAR ver esto bien dps el casteo hace falta?
    @Bean
    public LockProvider lockProvider(MongoTemplate mongoTemplate) {
        MongoDatabase db = mongoTemplate.getDb();
        return new MongoLockProvider(db);
    }
}
