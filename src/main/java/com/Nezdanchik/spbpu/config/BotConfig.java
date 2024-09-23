package com.Nezdanchik.spbpu.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource("classpath:bot.properties")
@Data
public class BotConfig {

    @Value("${botConfig.name}")
    private String name;

    @Value("${botConfig.token}")
    private String token;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> httpHeaders.add("X-API-KEY", "ea4c0a32-21a3-4c63-8d0e-a4a1bf41c20c"))
                .build();
    }

}
