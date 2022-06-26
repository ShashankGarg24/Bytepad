package com.Bytepad.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
public class Admin {

    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Email
    @Column(nullable = false)
    private String email;
    private String role;

    public Admin() {

    }

    public Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "ADMIN";
    }

}
