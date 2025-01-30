package org.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "teachers")
@EqualsAndHashCode(exclude = {"user", "courses"})
public class Teacher {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JoinColumn(name = "number_of_courses")
    public Integer numberOfCourses;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_teacher",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private Set<Course> courses = new HashSet<>();
}
