package com.praksix.Gnudnuz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.praksix.Gnudnuz.model.Nuz;
import com.praksix.Gnudnuz.repository.NuzRepository;

@Service
public class NuzService {

    @Autowired
    private NuzRepository nuzRepository;

    public Nuz createUser(Nuz nuz) {
        // Initialiser voteCount à 1 lors de la création
        nuz.setVoteCount(1);
        return nuzRepository.save(nuz);
    }

    public List<Nuz> getAllNuzs() {
        return nuzRepository.findAll();
    }

    public Nuz getNuzById(String id) {
        return nuzRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nuz not found with id: " + id));
    }

    public Nuz findByAuthor(String author) {
        return nuzRepository.findByAuthorId(author)
                .orElseThrow(() -> new RuntimeException("Nuz not found with author: " + author));
    }

   
    public Nuz getNuzWithMostVotes() {
        return nuzRepository.findTopByOrderByVoteCountDesc()
                .orElseThrow(() -> new RuntimeException("Aucun Nuz trouvé"));
    }

    public Nuz updateUser(String id, Nuz nuz) {
        Nuz existingNuz = getNuzById(id);
        existingNuz.setTitle(nuz.getTitle());
        existingNuz.setContent(nuz.getContent());
        existingNuz.setAuthorId(nuz.getAuthorId());
        existingNuz.setVoteCount(nuz.getVoteCount());
        existingNuz.setStatus(nuz.getStatus());
        return nuzRepository.save(existingNuz);
    }

    public void deleteNuz(String id) {
        nuzRepository.deleteById(id);
    }
} 