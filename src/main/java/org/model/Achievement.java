package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "description")
    public String description;

    @Column(name = "type_of_achievements")
    public Integer type;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "achievements_student",
            joinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
    private Set<Student> students = new HashSet<>();
}
