package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.TaskRequest;
import com.taskflow.taskflow_api.entity.Task;
import com.taskflow.taskflow_api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest req) {
        return ResponseEntity.ok(taskService.createTask(req));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getMyTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id,
            @Valid @RequestBody TaskRequest req) {
        return ResponseEntity.ok(taskService.updateTask(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
