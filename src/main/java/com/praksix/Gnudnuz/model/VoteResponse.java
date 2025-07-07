package com.praksix.Gnudnuz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {
    private boolean hasVoted;
    private int voteCount;
    private String message;
    
    public VoteResponse(boolean hasVoted) {
        this.hasVoted = hasVoted;
        this.voteCount = 0;
        this.message = hasVoted ? "Vote ajouté" : "Vote supprimé";
    }
    
    public VoteResponse(boolean hasVoted, int voteCount) {
        this.hasVoted = hasVoted;
        this.voteCount = voteCount;
        this.message = hasVoted ? "Vote ajouté" : "Vote supprimé";
    }
} 