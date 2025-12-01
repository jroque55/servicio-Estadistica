package com.metamapa.Config;

import com.mongodb.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mongodb.client.MongoClient;
// Import for MongoDatabase
import com.mongodb.client.MongoDatabase;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class SchedulerConfig {

    // 💡 Replace "your-database-name" with the actual name of your MongoDB database
    private static final String DATABASE_NAME = "your-database-name";

    @Bean
    public LockProvider lockProvider(MongoClient mongoClient) {
        // 1. Get the specific MongoDatabase instance from the MongoClient
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);

        // 2. Pass the MongoDatabase to the MongoLockProvider constructor
        return new MongoLockProvider(mongoDatabase);
    }
}

