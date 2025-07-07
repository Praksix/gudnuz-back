package com.praksix.Gnudnuz.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.praksix.Gnudnuz.model.Vote;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    
    // Trouver un vote spécifique par postId et authorId
    Vote findByPostIdAndAuthorId(String postId, String authorId);
    
    // Trouver tous les votes pour un post spécifique
    List<Vote> findByPostId(String postId);
    
    // Compter le nombre de votes pour un post
    long countByPostId(String postId);
    
    // Trouver tous les votes d'un utilisateur
    List<Vote> findByAuthorId(String authorId);
    
    // Vérifier si un utilisateur a déjà voté pour un post
    boolean existsByPostIdAndAuthorId(String postId, String authorId);
    
    // Supprimer tous les votes d'un post (utile pour la suppression d'un Nuz)
    void deleteByPostId(String postId);
    
    // Supprimer tous les votes d'un utilisateur (utile pour la suppression d'un compte)
    void deleteByAuthorId(String authorId);
} 