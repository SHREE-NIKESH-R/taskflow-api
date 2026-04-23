package com.taskflow.taskflow_api.dto;

import com.taskflow.taskflow_api.entity.Task.Priority;
import com.taskflow.taskflow_api.entity.Task.TaskStatus;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;

    @NotNull
    private Priority priority;

    private TaskStatus status;
    private LocalDateTime deadline;
}
