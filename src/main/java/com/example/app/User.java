package com.example.app;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "com.example.app.User")
@Table(name = "\"user\"")
@lombok.Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "from_id")
    private List<Message> messages = new LinkedList<>();

    @Override
    public String toString() {
        return "com.example.app.User: {\n" +
                "\tid: " + id + ",\n" +
                "\temail: '" + email + "',\n" +
                "\tpassword: '" + password + "',\n" +
                "\tname: '" + name + "',\n" +
                '}';
    }
}
