package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.dto.TaskRequest;
import com.taskflow.taskflow_api.entity.*;
import com.taskflow.taskflow_api.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final KafkaEventService kafkaEventService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Task createTask(TaskRequest req) {
        User user = getCurrentUser();
        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority())
                .status(req.getStatus() != null ? req.getStatus() : Task.TaskStatus.TODO)
                .deadline(req.getDeadline())
                .user(user).build();
        Task saved = taskRepository.save(task);
        kafkaEventService.publishTaskEvent("TASK_CREATED", saved.getId(), user.getEmail());
        return saved;
    }

    public List<Task> getMyTasks() {
        return taskRepository.findByUserIdOrderByPriorityDescCreatedAtDesc(getCurrentUser().getId());
    }

    public Task updateTask(Long id, TaskRequest req) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Access denied");
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setPriority(req.getPriority());
        if (req.getStatus() != null)
            task.setStatus(req.getStatus());
        task.setDeadline(req.getDeadline());
        Task saved = taskRepository.save(task);
        kafkaEventService.publishTaskEvent("TASK_UPDATED", id, user.getEmail());
        return saved;
    }

    public void deleteTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Access denied");
        taskRepository.delete(task);
        kafkaEventService.publishTaskEvent("TASK_DELETED", id, user.getEmail());
    }
}
