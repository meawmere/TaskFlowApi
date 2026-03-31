package dev.meawmere.taskflow.service;

import dev.meawmere.taskflow.exception.ResourceNotFoundException;
import dev.meawmere.taskflow.model.Task;
import dev.meawmere.taskflow.payload.TaskUpdateRequest;
import dev.meawmere.taskflow.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Transactional
    public Task updateTask(Long id, TaskUpdateRequest request, Long userId) throws ResourceNotFoundException, AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not founded"));

        if (!task.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to update this task");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        return taskRepository.save(task);
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
