package com.praksix.Gudnuz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@ComponentScan(basePackages = "com.praksix.Gudnuz")
public class AppConfig {
    // Configuration principale de l'application
} 