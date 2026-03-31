package dev.meawmere.taskflow.controller;

import dev.meawmere.taskflow.common.ApiResponse;
import dev.meawmere.taskflow.exception.TaskNotFoundException;
import dev.meawmere.taskflow.exception.UserNotFoundException;
import dev.meawmere.taskflow.model.Task;
import dev.meawmere.taskflow.payload.*;
import dev.meawmere.taskflow.security.UserDetailsImpl;
import dev.meawmere.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDetailsResponse>> details(@PathVariable(name = "id") Long taskId, @AuthenticationPrincipal UserDetailsImpl currentUser) throws AccessDeniedException, TaskNotFoundException {
        Task task = taskService.findDetails(taskId, currentUser.getId());
        TaskDetailsResponse response = TaskDetailsResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskCreateResponse>> create(@Valid @RequestBody TaskCreateRequest request, @AuthenticationPrincipal UserDetailsImpl currentUser) throws UserNotFoundException {
        Task task = taskService.create(request, currentUser.getId());
        TaskCreateResponse response = TaskCreateResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .title(task.getTitle())
                .createdAt(task.getCreatedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskUpdateResponse>> update(@PathVariable(name = "id") Long taskId, @Valid @RequestBody TaskUpdateRequest request, @AuthenticationPrincipal UserDetailsImpl currentUser) throws AccessDeniedException, TaskNotFoundException {
        Task task = taskService.update(taskId, request, currentUser.getId());
        TaskUpdateResponse response = TaskUpdateResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(name = "id") Long taskId, @AuthenticationPrincipal UserDetailsImpl currentUser) throws AccessDeniedException, TaskNotFoundException {
        taskService.delete(taskId, currentUser.getId());

        return ResponseEntity.ok(ApiResponse.success("The task was successfully deleted", null));
    }


    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
