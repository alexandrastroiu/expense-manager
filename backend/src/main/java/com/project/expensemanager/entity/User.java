package com.project.expensemanager.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Default constructor
    protected User() {}

    // Parameterized constructor
    public User(String username, String firstName, String lastName, String passwordHash, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.email = email;
    }
}