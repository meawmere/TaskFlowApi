package dev.meawmere.taskflow.controller;

import dev.meawmere.taskflow.common.ApiResponse;
import dev.meawmere.taskflow.exception.ResourceNotFoundException;
import dev.meawmere.taskflow.model.Task;
import dev.meawmere.taskflow.model.UserAccount;
import dev.meawmere.taskflow.payload.TaskUpdateRequest;
import dev.meawmere.taskflow.security.UserDetailsImpl;
import dev.meawmere.taskflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> update(@PathVariable(name = "id") Long taskId, @RequestBody TaskUpdateRequest request, @AuthenticationPrincipal UserDetailsImpl currentUser) throws AccessDeniedException, ResourceNotFoundException {
        Task task = taskService.updateTask(taskId, request, currentUser.getId());

        return ResponseEntity.ok(ApiResponse.success(task));
    }


    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
