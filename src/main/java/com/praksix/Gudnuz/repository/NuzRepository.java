package com.praksix.Gudnuz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.praksix.Gudnuz.model.Nuz;

@Repository
public interface NuzRepository extends MongoRepository<Nuz, String> {
    
    Optional<Nuz> findByAuthorId(String authorId);
    
    Optional<Nuz> findTopByOrderByVoteCountDesc();
    
    // Trouver les Nuz les plus votés avec une limite
    List<Nuz> findTopNByOrderByVoteCountDesc(Pageable pageable);
    
    // Trouver les Nuz par statut
    List<Nuz> findByStatus(String status);
    
    // Trouver les Nuz avec un nombre minimum de votes
    @Query("{'voteCount': {$gte: ?0}}")
    List<Nuz> findByVoteCountGreaterThanEqual(int minVotes);
    
    // Trouver les Nuz créés dans une période donnée
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Nuz> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    
    // Trouver les Nuz les plus récents
    List<Nuz> findTopNByOrderByCreatedAtDesc(Pageable pageable);
    
    // Compter les Nuz par auteur
    long countByAuthorId(String authorId);
    
    // Trouver tous les Nuz d'un auteur
    List<Nuz> findAllByAuthorId(String authorId);
    
    // Supprimer les Nuz créés avant une date donnée
    void deleteByCreatedAtBefore(java.time.LocalDateTime date);
} 