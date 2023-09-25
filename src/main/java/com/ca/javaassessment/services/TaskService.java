package com.ca.javaassessment.services;

import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.entities.User;
import com.ca.javaassessment.exceptions.NotFoundException;
import com.ca.javaassessment.repositories.TaskRepository;
import com.ca.javaassessment.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task createTask(Long userId, Task task) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        task.setUser(user);
        if (task.getStatus() == null) {
            task.setStatus(Task.Status.INITIATED);
        }
        return taskRepository.save(task);
    }

    public Task getTaskById(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new NotFoundException("Task not found for the given user"));
    }

    public Task updateTask(Long userId, Long taskId, Task taskToUpdate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task existingTask = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new NotFoundException("Task not found for the given user"));

        existingTask.setName(taskToUpdate.getName());
        existingTask.setDescription(taskToUpdate.getDescription());
        existingTask.setDateTime(taskToUpdate.getDateTime());
        existingTask.setStatus(taskToUpdate.getStatus());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task existingTask = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new NotFoundException("Task not found for the given user"));

        taskRepository.delete(existingTask);
    }

    public Page<Task> listPaginatedTasks(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Page<Task> tasks = taskRepository.findAllByUser(user, pageable);
        return tasks != null ? tasks : Page.empty();
    }

}
