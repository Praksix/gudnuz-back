package com.praksix.Gnudnuz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.praksix.Gnudnuz.model.Vote;
import com.praksix.Gnudnuz.model.VoteResponse;
import com.praksix.Gnudnuz.model.Nuz;
import com.praksix.Gnudnuz.repository.VoteRepository;
import com.praksix.Gnudnuz.repository.NuzRepository;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    
    @Autowired
    private NuzRepository nuzRepository;

    /**
     * Créer un nouveau vote
     */
    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
    }

    /**
     * Récupérer tous les votes
     */
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    /**
     * Basculer le vote d'un utilisateur pour un post (ajouter/supprimer)
     */
    public VoteResponse toggleVote(String postId, String authorId) {
        try {
            System.out.println("Toggle vote - postId: " + postId + ", authorId: " + authorId);
            
            Vote existingVote = voteRepository.findByPostIdAndAuthorId(postId, authorId);
            System.out.println("Existing vote found: " + (existingVote != null));
            
            if (existingVote != null) {
                // L'utilisateur a déjà voté, on supprime le vote
                voteRepository.delete(existingVote);
                updateNuzVoteCount(postId, -1);
                int newVoteCount = (int) voteRepository.countByPostId(postId);
                System.out.println("Vote removed, new count: " + newVoteCount);
                return new VoteResponse(false, newVoteCount);
            } else {
                // L'utilisateur n'a pas encore voté, on ajoute le vote
                Vote newVote = new Vote();
                newVote.setPostId(postId);
                newVote.setAuthorId(authorId);
                voteRepository.save(newVote);
                updateNuzVoteCount(postId, 1);
                int newVoteCount = (int) voteRepository.countByPostId(postId);
                System.out.println("Vote added, new count: " + newVoteCount);
                return new VoteResponse(true, newVoteCount);
            }
        } catch (Exception e) {
            System.err.println("Error in toggleVote: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Mettre à jour le compteur de votes d'un Nuz
     */
    private void updateNuzVoteCount(String postId, int increment) {
        Optional<Nuz> nuzOpt = nuzRepository.findById(postId);
        if (nuzOpt.isPresent()) {
            Nuz nuz = nuzOpt.get();
            nuz.setVoteCount(nuz.getVoteCount() + increment);
            nuzRepository.save(nuz);
        }
    }

    /**
     * Récupérer tous les votes pour un post spécifique
     */
    public List<Vote> getVotesByPostId(String postId) {
        return voteRepository.findByPostId(postId);
    }
    
    /**
     * Récupérer tous les votes d'un utilisateur
     */
    public List<Vote> getVotesByAuthorId(String authorId) {
        return voteRepository.findByAuthorId(authorId);
    }
    
    /**
     * Compter le nombre de votes pour un post
     */
    public long getVoteCountByPostId(String postId) {
        return voteRepository.countByPostId(postId);
    }
    
    /**
     * Vérifier si un utilisateur a déjà voté pour un post
     */
    public boolean hasUserVoted(String postId, String authorId) {
        return voteRepository.existsByPostIdAndAuthorId(postId, authorId);
    }
    
    /**
     * Supprimer un vote spécifique
     */
    public void deleteVote(String voteId) {
        Optional<Vote> voteOpt = voteRepository.findById(voteId);
        if (voteOpt.isPresent()) {
            Vote vote = voteOpt.get();
            voteRepository.delete(vote);
            updateNuzVoteCount(vote.getPostId(), -1);
        }
    }
    
    /**
     * Supprimer tous les votes d'un post (utile lors de la suppression d'un Nuz)
     */
    public void deleteVotesByPostId(String postId) {
        voteRepository.deleteByPostId(postId);
    }
    
    /**
     * Supprimer tous les votes d'un utilisateur (utile lors de la suppression d'un compte)
     */
    public void deleteVotesByAuthorId(String authorId) {
        voteRepository.deleteByAuthorId(authorId);
    }
    
    /**
     * Récupérer le Nuz avec le plus de votes (pour l'élection toutes les 12h)
     */
    public Nuz getNuzWithMostVotes() {
        return nuzRepository.findTopByOrderByVoteCountDesc()
                .orElseThrow(() -> new RuntimeException("Aucun Nuz trouvé"));
    }
    
    /**
     * Récupérer les Nuz les plus votés (top N)
     */
    public List<Nuz> getTopVotedNuzs(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return nuzRepository.findTopNByOrderByVoteCountDesc(pageable);
    }
    
    /**
     * Récupérer un vote par son ID
     */
    public Vote getVoteById(String voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("Vote non trouvé avec l'id: " + voteId));
    }
    
    /**
     * Récupérer les statistiques de vote pour un post
     */
    public VoteStats getVoteStats(String postId) {
        long totalVotes = voteRepository.countByPostId(postId);
        List<Vote> votes = voteRepository.findByPostId(postId);
        
        return new VoteStats(postId, totalVotes, votes);
    }
    
    /**
     * Classe interne pour les statistiques de vote
     */
    public static class VoteStats {
        private String postId;
        private long totalVotes;
        private List<Vote> votes;
        
        public VoteStats(String postId, long totalVotes, List<Vote> votes) {
            this.postId = postId;
            this.totalVotes = totalVotes;
            this.votes = votes;
        }
        
        // Getters
        public String getPostId() { return postId; }
        public long getTotalVotes() { return totalVotes; }
        public List<Vote> getVotes() { return votes; }
    }
} 