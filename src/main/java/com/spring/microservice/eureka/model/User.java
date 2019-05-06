package com.spring.microservice.eureka.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "users")
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username field is required")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotEmpty(message = "Password field required")
    @Column(name = "password", nullable = false)
    private String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}