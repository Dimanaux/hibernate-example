package com.example.app.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "com.example.app.models.Message")
@Table(name = "message")
@lombok.Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    //    @ManyToOne(targetEntity = com.example.app.models.User.class)
    @ManyToOne
    @JoinColumn(name = "from_id")
//    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private User from;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "recipient",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> recipients = new LinkedList<>();

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Override
    public String toString() {
        return "com.example.app.models.Message: {\n" +
                "\tid: " + id + ",\n" +
                "\tfrom: '" + from.getEmail() + "',\n" +
                "\tsubject: '" + subject + "',\n" +
                "\trecipients: [" + recipients.stream()
                                        .map(User::getEmail)
                                        .collect(Collectors.joining(", "))
                + "],\n" +
                "\tcontent: '" + content + "',\n" +
                "\tsentAt: '" + sentAt + "'\n" +
                '}';
    }
}
