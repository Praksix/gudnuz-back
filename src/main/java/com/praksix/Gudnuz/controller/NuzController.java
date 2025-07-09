package com.praksix.Gudnuz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.praksix.Gudnuz.model.Nuz;
import com.praksix.Gudnuz.model.User;
import com.praksix.Gudnuz.service.NuzService;
import com.praksix.Gudnuz.service.UserService;

@RestController
@CrossOrigin(origins = "*") // Pour permettre les requêtes CORS
@RequestMapping("/api/nuzs")
public class NuzController {

    @Autowired
    private NuzService nuzService;
    
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nuz createNuz(@RequestBody Nuz nuz) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);
        
        // Définir l'auteur du Nuz
        nuz.setAuthorId(currentUser.getId());
        // Utiliser le vrai username au lieu de l'email
        nuz.setAuthorUsername(currentUser.getDisplayUsername());
        nuz.setVoteCount(0);
        
        return nuzService.createUser(nuz);
    }

    @GetMapping
    public List<Nuz> getAllNuzs() {
        return nuzService.getAllNuzs();
    }

    @GetMapping("/{id}")
    public Nuz getNuzById(@PathVariable String id) {
        return nuzService.getNuzById(id);
    }

    @GetMapping("/author/{author}")
    public Nuz getNuzByAuthor(@PathVariable String author) {
        return nuzService.findByAuthor(author);
    }

    @GetMapping("/votes/most-voted")
    public Nuz getNuzWithMostVotes() {
        return nuzService.getNuzWithMostVotes();
    }

    @DeleteMapping("/{id}")
    public void deleteNuz(@PathVariable String id) {
        nuzService.deleteNuz(id);
    }
}
