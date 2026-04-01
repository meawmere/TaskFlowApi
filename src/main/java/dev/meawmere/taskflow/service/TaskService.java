package dev.meawmere.taskflow.service;

import dev.meawmere.taskflow.exception.TaskNotFoundException;
import dev.meawmere.taskflow.exception.UserNotFoundException;
import dev.meawmere.taskflow.model.Task;
import dev.meawmere.taskflow.model.UserAccount;
import dev.meawmere.taskflow.payload.TaskCreateRequest;
import dev.meawmere.taskflow.payload.TaskUpdateRequest;
import dev.meawmere.taskflow.repository.TaskRepository;
import dev.meawmere.taskflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Transactional
    public List<Task> findAllUserTasks(Long userId) {
        return taskRepository.findTasksByUserAccountId(userId);
    }

    @Transactional
    public Task findUserTask(Long taskId, Long userId) throws TaskNotFoundException, AccessDeniedException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!task.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        return task;
    }

    @Transactional
    public Task create(TaskCreateRequest request, Long userId) throws UserNotFoundException {
        UserAccount user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserAccount(user);

        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long taskId, TaskUpdateRequest request, Long userId) throws TaskNotFoundException, AccessDeniedException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not founded"));

        if (!task.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long taskId, Long userId) throws TaskNotFoundException, AccessDeniedException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not founded"));

        if (!task.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        taskRepository.delete(task);
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
