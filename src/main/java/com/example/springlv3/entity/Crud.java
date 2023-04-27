package com.example.springlv3.entity;
import com.example.springlv3.dto.CrudRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Crud extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crudId")
    //pirvate -> controller에 못씀 : method -> Getter
    private Long id;
    //private이기 때문에 controller에서 못쓰니까 method -> Getter
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    //외래키
//    @OneToMany(mappedBy = "crud")
   // @JoinColumn(name = "userId", nullable = false)
    private List<Comment> comment;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "crud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    public Crud(CrudRequestDto requestDto)  {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
    public void update(CrudRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void addUser(Users users){
        this.users = users;
    }


}

