package org.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(name = "file")
    public String file;

    @Column(name = "video")
    public String video;

    @Column(name = "picture")
    public String picture;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "step_attachments",
            joinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "step_id", referencedColumnName = "id"))
    private Set<Step> steps = new HashSet<>();
}
