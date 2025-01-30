package org.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "students")
@EqualsAndHashCode(exclude = {"user", "courses"})
public class Student {

    @Id
    @GeneratedValue()
    private Integer id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_student",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "achievements_student",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "id"))
    private Set<Achievement> achievements = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    @JoinColumn(name = "number_of_courses")
    public Integer numberOfCourses;
}
