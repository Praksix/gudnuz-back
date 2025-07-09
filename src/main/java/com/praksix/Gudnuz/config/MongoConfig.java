package com.praksix.Gudnuz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MongoConfig implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Créer l'index TTL pour la collection nuzs
        Index ttlIndex = new Index()
                .on("createdAt", org.springframework.data.domain.Sort.Direction.ASC)
                .expire(Duration.ofHours(24));
        
        try {
            mongoTemplate.indexOps("nuzs").ensureIndex(ttlIndex);
            System.out.println("Index TTL créé avec succès pour la collection nuzs");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'index TTL: " + e.getMessage());
        }
    }
} 