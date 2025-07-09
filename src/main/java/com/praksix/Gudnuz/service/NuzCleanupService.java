package com.praksix.Gudnuz.service;

import com.praksix.Gudnuz.repository.NuzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NuzCleanupService {

    @Autowired
    private NuzRepository nuzRepository;

    // Exécuter toutes les heures
    @Scheduled(fixedRate = 3600000) // 1 heure en millisecondes
    public void cleanupOldNuzs() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        
        // Supprimer tous les Nuz créés il y a plus de 24h
        nuzRepository.deleteByCreatedAtBefore(cutoffTime);
        
        System.out.println("Nettoyage des Nuz terminé - supprimé les Nuz de plus de 24h");
    }
} 