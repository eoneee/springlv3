package com.example.springlv3.dto;


import com.example.springlv3.entity.Comment;
import com.example.springlv3.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto{
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public  CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username =comment.getUsers().getUsername();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
