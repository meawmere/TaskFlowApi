package dev.meawmere.taskflow.controller;

import dev.meawmere.taskflow.common.ApiResponse;
import dev.meawmere.taskflow.exception.TaskNotFoundException;
import dev.meawmere.taskflow.exception.UserNotFoundException;
import dev.meawmere.taskflow.model.Task;
import dev.meawmere.taskflow.payload.*;
import dev.meawmere.taskflow.security.UserDetailsImpl;
import dev.meawmere.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDetailsResponse>>> listTasks(@AuthenticationPrincipal UserDetailsImpl currentUser) throws TaskNotFoundException {
        List<Task> tasks = taskService.findAllUserTasks(currentUser.getId());
        List<TaskDetailsResponse> tasksDetails = tasks.stream().map(this::toDetailsResponse).toList();

        return ResponseEntity.ok(ApiResponse.success(tasksDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDetailsResponse>> getTask(@PathVariable(name = "id") Long taskId, @AuthenticationPrincipal UserDetailsImpl currentUser) throws TaskNotFoundException {
        Task task = taskService.findUserTask(taskId, currentUser.getId());
        TaskDetailsResponse response = toDetailsResponse(task);

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

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(response));
    }

    @PatchMapping("/{id}")
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
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long taskId, @AuthenticationPrincipal UserDetailsImpl currentUser) throws AccessDeniedException, TaskNotFoundException {
        taskService.delete(taskId, currentUser.getId());

        return ResponseEntity.noContent().build();
    }

    private TaskDetailsResponse toDetailsResponse(Task task) {
        return TaskDetailsResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}
