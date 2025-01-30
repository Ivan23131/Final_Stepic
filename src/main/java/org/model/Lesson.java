package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "lessons")
@Entity
public class Lesson {
    @Id
    @GeneratedValue
    public Integer id;


    @Column(name = "name")
    public String name;

    @Column(name = "count_of_steps")
    public Integer countOfSteps;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id")
    private Set<Step> steps = new HashSet<>();
}
