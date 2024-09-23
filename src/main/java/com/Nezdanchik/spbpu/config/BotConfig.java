package com.Nezdanchik.spbpu.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration

@PropertySource("classpath:bot.properties")
@Data
public class BotConfig {

    @Value("${botConfig.name}")
    private String name;

    @Value("${botConfig.token}")
    private String token;

}
