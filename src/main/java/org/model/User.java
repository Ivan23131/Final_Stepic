package org.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = {"teacher", "student"})
public class User {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name = "created_date")
    public LocalDateTime createdDate;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Teacher teacher;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Student student;
}