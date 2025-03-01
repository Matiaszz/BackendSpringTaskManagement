package dev.matias.TaskManagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 35, unique = true)
    private String username;


    @Column(nullable = false)
    private String role;

    private String name;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    @Lob
    private String description;

    private String profileImageURL = "https://imgs.search.brave.com/1WFIpUNAOtVXo51SuasJnMAgOsPwQQXErqrO6H1Ps1M/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzk4LzFk/LzZiLzk4MWQ2YjJl/MGNjYjVlOTY4YTA2/MThjOGQ0NzY3MWRh/LmpwZw";
}
