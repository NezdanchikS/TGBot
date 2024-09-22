package com.Nezdanchik.spbpu.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.Nezdanchik.spbpu")
@PropertySource("classpath:bot.properties")
public class BotConfig {

    @Value("${botConfig.name}")
    private String name;

    @Value("${botConfig.token}")
    private String token;

    @Value("${botConfig.url}")
    private String url;

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getUrl() {
        return url;
    }
}
