package com.praksix.Gudnuz.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.praksix.Gudnuz.model.Nuz;
import com.praksix.Gudnuz.repository.NuzRepository;

@Service
public class ElectionService {

    @Autowired
    private NuzRepository nuzRepository;
    
    @Autowired
    private VoteService voteService;

    /**
     * Élection automatique toutes les 12h (à 6h et 18h)
     */
    @Scheduled(cron = "0 0 8,20 * * ?")
    public void electWinningNuz() {
        try {
            // Récupérer le Nuz avec le plus de votes
            Nuz winningNuz = voteService.getNuzWithMostVotes();
            
            if (winningNuz != null && winningNuz.getVoteCount() > 0) {
                // Marquer le Nuz comme élu
                winningNuz.setStatus("ELECTED");
                
                // Ajouter 12 heures à la durée de vie du Nuz élu
                LocalDateTime newExpiryTime = winningNuz.getCreatedAt().plusHours(12);
                winningNuz.setCreatedAt(newExpiryTime);
                
                nuzRepository.save(winningNuz);
                
                // Envoyer le Nuz élu à tous les utilisateurs
                sendElectedNuzToAllUsers(winningNuz);
                
                // Réinitialiser les votes pour la prochaine élection
                resetVotesForNextElection();
                
                System.out.println("Nuz élu: " + winningNuz.getTitle() + " avec " + winningNuz.getVoteCount() + " votes");
                System.out.println("Nouvelle durée de vie: " + newExpiryTime);
            } else {
                System.out.println("Aucun Nuz éligible pour l'élection");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'élection: " + e.getMessage());
        }
    }
    
    /**
     * Envoyer le Nuz élu à tous les utilisateurs
     */
    private void sendElectedNuzToAllUsers(Nuz electedNuz) {
        // TODO: Implémenter l'envoi de notification
        // Cela pourrait être via email, push notification, websocket, etc.
        System.out.println("Envoi du Nuz élu '" + electedNuz.getTitle() + "' à tous les utilisateurs");
    }
    
    /**
     * Réinitialiser les votes pour la prochaine élection
     */
    private void resetVotesForNextElection() {
        // Marquer tous les Nuz non-élus comme "EN_ATTENTE" pour la prochaine élection
        List<Nuz> pendingNuzs = nuzRepository.findByStatus("PENDING");
        for (Nuz nuz : pendingNuzs) {
            nuz.setStatus("EN_ATTENTE");
            nuzRepository.save(nuz);
        }
    }
    
    /**
     * Récupérer le dernier Nuz élu
     */
    public Nuz getLastElectedNuz() {
        return nuzRepository.findByStatus("ELECTED")
                .stream()
                .max((n1, n2) -> n1.getCreatedAt().compareTo(n2.getCreatedAt()))
                .orElse(null);
    }
    
    /**
     * Récupérer tous les Nuz élus
     */
    public List<Nuz> getAllElectedNuzs() {
        return nuzRepository.findByStatus("ELECTED");
    }
    
    /**
     * Récupérer les Nuz en attente d'élection
     */
    public List<Nuz> getPendingNuzs() {
        return nuzRepository.findByStatus("PENDING");
    }
    
    /**
     * Forcer une élection manuelle (pour les tests ou cas spéciaux)
     */
    public Nuz forceElection() {
        electWinningNuz();
        return getLastElectedNuz();
    }
    
    /**
     * Récupérer les statistiques d'élection
     */
    public ElectionStats getElectionStats() {
        long totalElected = nuzRepository.findByStatus("ELECTED").size();
        long totalPending = nuzRepository.findByStatus("PENDING").size();
        Nuz lastElected = getLastElectedNuz();
        
        return new ElectionStats(totalElected, totalPending, lastElected);
    }
    
    /**
     * Classe pour les statistiques d'élection
     */
    public static class ElectionStats {
        private long totalElected;
        private long totalPending;
        private Nuz lastElected;
        
        public ElectionStats(long totalElected, long totalPending, Nuz lastElected) {
            this.totalElected = totalElected;
            this.totalPending = totalPending;
            this.lastElected = lastElected;
        }
        
        // Getters
        public long getTotalElected() { return totalElected; }
        public long getTotalPending() { return totalPending; }
        public Nuz getLastElected() { return lastElected; }
    }
} 