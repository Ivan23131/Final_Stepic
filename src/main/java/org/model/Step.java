package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "steps")
@Data
public class Step {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "name")
    public String name;

    @OneToMany
    @JoinColumn(name = "step_id")
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "step_attachments",
            joinColumns = @JoinColumn(name = "step_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id"))
    private Set<Attachment> attachments = new HashSet<>();

}
