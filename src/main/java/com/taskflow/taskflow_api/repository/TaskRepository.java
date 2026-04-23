package com.taskflow.taskflow_api.repository;

import com.taskflow.taskflow_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdOrderByPriorityDescCreatedAtDesc(Long userId);

    List<Task> findByUserIdAndStatus(Long userId, Task.TaskStatus status);

    List<Task> findByUserIdAndStatusOrderByPriorityDescCreatedAtDesc(Long userId, Task.TaskStatus status);
}
