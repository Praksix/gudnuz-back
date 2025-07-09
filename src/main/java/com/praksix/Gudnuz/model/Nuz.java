package com.praksix.Gudnuz.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

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
    @Indexed(expireAfterSeconds = 86400) // 24h = 86400 secondes
    private LocalDateTime createdAt;
    
    private String status;

}
