package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "count_of_lesson")
    public Integer countOfLesson;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chapter_id")
    private Set<Lesson> lessons = new HashSet<>();
}
