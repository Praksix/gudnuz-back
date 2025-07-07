package com.praksix.Gnudnuz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.praksix.Gnudnuz.model.Vote;
import com.praksix.Gnudnuz.model.VoteResponse;
import com.praksix.Gnudnuz.model.Nuz;
import com.praksix.Gnudnuz.service.VoteService;
import com.praksix.Gnudnuz.service.ElectionService;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "*") // Pour permettre les requêtes CORS
public class VoteController {

    @Autowired
    private VoteService voteService;
    
    @Autowired
    private ElectionService electionService;

    /**
     * POST /api/votes/toggle
     * Basculer le vote d'un utilisateur pour un post
     */
    @PostMapping("/toggle")
    public ResponseEntity<VoteResponse> toggleVote(
            @RequestParam String postId,
            @RequestParam String authorId) {
        try {
            VoteResponse response = voteService.toggleVote(postId, authorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new VoteResponse(false, 0, "Erreur lors du vote: " + e.getMessage()));
        }
    }

    /**
     * POST /api/votes
     * Créer un nouveau vote
     */
    @PostMapping
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
        try {
            Vote createdVote = voteService.createVote(vote);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * GET /api/votes
     * Récupérer tous les votes
     */
    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes() {
        try {
            List<Vote> votes = voteService.getAllVotes();
            return ResponseEntity.ok(votes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/{voteId}
     * Récupérer un vote par son ID
     */
    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> getVoteById(@PathVariable String voteId) {
        try {
            Vote vote = voteService.getVoteById(voteId);
            return ResponseEntity.ok(vote);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/votes/{voteId}
     * Supprimer un vote
     */
    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> deleteVote(@PathVariable String voteId) {
        try {
            voteService.deleteVote(voteId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/votes/post/{postId}
     * Récupérer tous les votes d'un post
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Vote>> getVotesByPostId(@PathVariable String postId) {
        try {
            List<Vote> votes = voteService.getVotesByPostId(postId);
            return ResponseEntity.ok(votes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/user/{authorId}
     * Récupérer tous les votes d'un utilisateur
     */
    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<Vote>> getVotesByAuthorId(@PathVariable String authorId) {
        try {
            List<Vote> votes = voteService.getVotesByAuthorId(authorId);
            return ResponseEntity.ok(votes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/count/{postId}
     * Compter les votes d'un post
     */
    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> getVoteCountByPostId(@PathVariable String postId) {
        try {
            long count = voteService.getVoteCountByPostId(postId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/check
     * Vérifier si un utilisateur a voté pour un post
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserVoted(
            @RequestParam String postId,
            @RequestParam String authorId) {
        try {
            boolean hasVoted = voteService.hasUserVoted(postId, authorId);
            return ResponseEntity.ok(hasVoted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/stats/{postId}
     * Récupérer les statistiques de vote d'un post
     */
    @GetMapping("/stats/{postId}")
    public ResponseEntity<VoteService.VoteStats> getVoteStats(@PathVariable String postId) {
        try {
            VoteService.VoteStats stats = voteService.getVoteStats(postId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/votes/post/{postId}
     * Supprimer tous les votes d'un post
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deleteVotesByPostId(@PathVariable String postId) {
        try {
            voteService.deleteVotesByPostId(postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/votes/user/{authorId}
     * Supprimer tous les votes d'un utilisateur
     */
    @DeleteMapping("/user/{authorId}")
    public ResponseEntity<Void> deleteVotesByAuthorId(@PathVariable String authorId) {
        try {
            voteService.deleteVotesByAuthorId(authorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===== ENDPOINTS POUR L'ÉLECTION =====

    /**
     * GET /api/votes/winner
     * Récupérer le Nuz avec le plus de votes
     */
    @GetMapping("/winner")
    public ResponseEntity<Nuz> getNuzWithMostVotes() {
        try {
            Nuz winner = voteService.getNuzWithMostVotes();
            return ResponseEntity.ok(winner);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/votes/top
     * Récupérer les N Nuz les plus votés
     */
    @GetMapping("/top")
    public ResponseEntity<List<Nuz>> getTopVotedNuzs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Nuz> topNuzs = voteService.getTopVotedNuzs(limit);
            return ResponseEntity.ok(topNuzs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/votes/election/force
     * Forcer une élection manuelle (admin seulement)
     */
    @PostMapping("/election/force")
    public ResponseEntity<Nuz> forceElection() {
        try {
            Nuz electedNuz = electionService.forceElection();
            return ResponseEntity.ok(electedNuz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/election/stats
     * Récupérer les statistiques d'élection
     */
    @GetMapping("/election/stats")
    public ResponseEntity<ElectionService.ElectionStats> getElectionStats() {
        try {
            ElectionService.ElectionStats stats = electionService.getElectionStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/election/last
     * Récupérer le dernier Nuz élu
     */
    @GetMapping("/election/last")
    public ResponseEntity<Nuz> getLastElectedNuz() {
        try {
            Nuz lastElected = electionService.getLastElectedNuz();
            if (lastElected != null) {
                return ResponseEntity.ok(lastElected);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/election/all
     * Récupérer tous les Nuz élus
     */
    @GetMapping("/election/all")
    public ResponseEntity<List<Nuz>> getAllElectedNuzs() {
        try {
            List<Nuz> electedNuzs = electionService.getAllElectedNuzs();
            return ResponseEntity.ok(electedNuzs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/votes/election/pending
     * Récupérer les Nuz en attente d'élection
     */
    @GetMapping("/election/pending")
    public ResponseEntity<List<Nuz>> getPendingNuzs() {
        try {
            List<Nuz> pendingNuzs = electionService.getPendingNuzs();
            return ResponseEntity.ok(pendingNuzs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 