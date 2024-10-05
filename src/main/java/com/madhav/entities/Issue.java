package com.madhav.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String status;

    private Long projectId;

    private String priority;

    private LocalDate dueDate;

    private List<String> tags = new ArrayList<>();

    // May Issues may one same user
    @ManyToOne
    private User assignee;

    // One project can have many issues
    @JsonIgnore
    @ManyToOne
    private Project project;

    @JsonIgnore
    @OneToMany(mappedBy = "issue",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList();
}
