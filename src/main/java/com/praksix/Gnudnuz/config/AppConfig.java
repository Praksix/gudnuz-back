package com.praksix.Gnudnuz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@ComponentScan(basePackages = "com.praksix.Gnudnuz")
public class AppConfig {
    // Configuration principale de l'application
} 