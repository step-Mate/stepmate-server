package server.stepmate.account.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String password;

    private String userName;

    private Integer age;

    private Integer height;

    private Integer weight;

    @Builder
    public User(String userId, String password, String userName, Integer age, Integer height, Integer weight) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }
}
