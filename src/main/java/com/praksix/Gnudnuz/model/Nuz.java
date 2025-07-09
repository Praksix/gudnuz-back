package com.praksix.Gnudnuz.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document(collection = "nuzs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nuz {

    @Id
    private String id;
    private String title;
    private String content;
    private String authorId;
    private String authorUsername;
    private Integer voteCount;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private String status;

}
