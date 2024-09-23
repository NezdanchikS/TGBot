package com.Nezdanchik.spbpu.config;

import com.mongodb.MongoClient;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    @SneakyThrows
    public MongoClient mongoClient() {
        return new MongoClient();
    }

}
