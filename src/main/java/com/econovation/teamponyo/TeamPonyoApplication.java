package com.econovation.teamponyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TeamPonyoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamPonyoApplication.class, args);
    }
}