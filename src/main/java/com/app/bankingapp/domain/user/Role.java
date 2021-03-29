package com.app.bankingapp.domain.user;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "users")
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
