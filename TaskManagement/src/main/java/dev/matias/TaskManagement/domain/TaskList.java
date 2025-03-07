package dev.matias.TaskManagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.services.AuthorizationService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_task_list")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private User owner;

    @Column(length = 40)
    private String title;

    @Column(length = 80)
    private String shortDescription;

    @Lob
    private String longDescription;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    private int percentageProgress = 0;

    private String color = "#ffffff";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public TaskList(AuthorizationService authorizationService, String title, String shortDescription, String longDescription){
        this.owner = (User) authorizationService.getLoggedUser();
        this.title = (title != null) ? title : "Untitled";
        this.shortDescription = (shortDescription != null) ? shortDescription : "";
        this.longDescription = (longDescription != null) ? longDescription : "";
    }

    public void addTask(Task task){
        tasks.add(task);
        task.setTaskList(this);
    }

    public void removeTask(Task task){
        tasks.remove(task);
        task.setTaskList(null);
    }

    public void updateProgress(){
        if (tasks.isEmpty()){
            percentageProgress = 0;
            return;
        }

        long completed = tasks.stream().filter(Task::getIsDone).count();
        this.percentageProgress = (int) ((completed * 100) / tasks.size());
    }
}
