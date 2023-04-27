package com.example.springlv3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
@Entity(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    //Enum의 선언된 상수의 이름을 string으로 변환하여 DB에 주입
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    //    public User(String username, String password) {
    public Users(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 유저랑 Crud랑 단방향으로 설정하셨어서 유저랑 Comment도 단방향으로 해야되요.
    // 오류 보니까 Users 랑 Comment 사이에서 순환참조가 일어나서 stackoverflow 가 발생했던 거에요.
    // 그래서 만약 내가 Users 랑 Crud 그리고 Users 랑 Comment 도 양방향을 하고 싶으면 아래 형식으로
    // @JsonManagedReference 사용해서 Comment 쪽엔 똑같이 @JsonBackReference 로 받아주면서 순환참조를 막으시면 되는데
    // 지금 프로젝트에선 회원탈퇴가 없기 때문에 단방향으로 하시는 게 편하시고 깔끔하실 거 에요.
    // 제가 7시에 일이 있어서 가봐야되서 장문의 편지 남깁니다.
    // 앉자말자 포스트맨 돌려서 되시든 안되시든 저한테 연락 주시면 감사하겠슴둥!
    // 누군가가 수정해주다 코드 더 복잡해지고 하니까 알아보기 빡세네요. 안되시면 젤 먼저 그냥 저한테 찾아오세요
    // 늦어지면 일 2~3배 복잡해지니까!! 먼저 안찾아와야겠다 이제,, 댓글 다 잘 작동되셔도 바로 제출하시지 말고
    // 공부하시고 넘어가세요. 이거 작동원리 제대로 모르고 넘어가면 4단계할 때 진짜 힘듭니다.
    // 안좋게 말하면 페어한테 민폐가 될 수 있어요. 우리가 하는 건 공부가 목적이어야 됩니다. 구현이 목적이 아닙니다.
    // 제가 도와드려서 쉽게 갈 순 있으나 본인이 알고 있다고 착각하면 안됩니다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // 꼭 두분이서 코드 뜯어보면서 모르겠으면 구글링해서 같이 공부해보고 하는 시간 가져야 됩니다.
    // 작동원리는 100% 이해하시고 가야됩니다. 어떨 때 어떤 걸 쓰는 지는 대충알면 되구,, 우리의 목적은 공부!!! 잊지마세요.
    // 과제가 단계별로 나눠져 있는 것은 이유가 있습니다잉~ T 로서 장문으로 조언 남기고 사라지겠슴둥.
    // 나중에 또 확인하러 올 겁니다.

//    @OneToMany(mappedBy = "crud", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comment = new ArrayList<>();


//    public User(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
}