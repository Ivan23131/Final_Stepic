package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "text")
    public String text;

    @Column(name = "date")
    public LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;
}
