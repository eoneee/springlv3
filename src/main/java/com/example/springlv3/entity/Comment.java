package com.example.springlv3.entity;


import com.example.springlv3.dto.CommentRequestDto;
import com.example.springlv3.dto.CrudAndComment;
import com.example.springlv3.dto.CrudRequestDto;
import com.example.springlv3.repository.CommentRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {  //  implements CrudAndComment 필요없는 애라 지우시면 되요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "crudId", nullable = false)
    private Crud crud;

    public Comment(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

//    public Comment(CommentRequestDto requestDto)  {
//        this.content = requestDto.getContent();
//    }



    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void addUser(Users users){ this.users = users; }

    public void addCrud(Crud crud){ this.crud = crud; }
}