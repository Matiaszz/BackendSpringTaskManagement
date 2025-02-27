package dev.matias.TaskManagement.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 25)
    private String name;

    @Column(length = 80)
    private String shortDescription;

    @Lob
    private String longDescription;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isDone = false;

    @ManyToOne
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;

    public Task(String name, String shortDescription, String longDescription, TaskList taskList) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.taskList = taskList;
    }
}
