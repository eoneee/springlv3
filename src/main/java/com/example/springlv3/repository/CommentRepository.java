package com.example.springlv3.repository;


import com.example.springlv3.entity.Comment;
import com.example.springlv3.entity.Crud;
import com.example.springlv3.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCrud(Crud crud);
//    Optional<Comment> findByUserAndCrud(Users users, Crud crud);

}
