package com.praksix.Gnudnuz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.praksix.Gnudnuz.model.Nuz;
import com.praksix.Gnudnuz.service.NuzService;

@RestController
@CrossOrigin(origins = "*") // Pour permettre les requêtes CORS
@RequestMapping("/api/nuzs")
public class NuzController {

    @Autowired
    private NuzService nuzService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nuz createNuz(@RequestBody Nuz nuz) {
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
