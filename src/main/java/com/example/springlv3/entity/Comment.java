//package com.example.springlv3.entity;
//
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Getter
//@Entity
//@NoArgsConstructor
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;
//
//    @Column(nullable = false)
//    private String contents;
//
//    @ManyToOne
//    @JoinColumn(name = "crud_id")
//    @Column(nullable = false)
//    private Crud crud;
//
//}