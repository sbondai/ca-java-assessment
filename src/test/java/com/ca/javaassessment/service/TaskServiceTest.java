package com.ca.javaassessment.service;

import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.entities.User;
import com.ca.javaassessment.exceptions.NotFoundException;
import com.ca.javaassessment.repositories.TaskRepository;
import com.ca.javaassessment.repositories.UserRepository;
import com.ca.javaassessment.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        Long userId = 1L;
        User user = new User();
        Task task = new Task();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(userId, task);

        assertEquals(task, result);
        assertEquals(user, result.getUser());
        assertEquals(Task.Status.INITIATED, result.getStatus());
    }

    @Test
    public void testGetTaskById() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();
        Task task = new Task();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(userId, taskId);

        assertEquals(task, result);
    }

    @Test
    public void testUpdateTask() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();
        Task existingTask = new Task();
        existingTask.setName("Old Name");
        existingTask.setDescription("Old Description");
        existingTask.setDateTime(LocalDateTime.now().minusDays(1));
        existingTask.setStatus(Task.Status.INPROGRESS);

        Task taskToUpdate = new Task();
        taskToUpdate.setName("New Name");
        taskToUpdate.setDescription("New Description");
        taskToUpdate.setDateTime(LocalDateTime.now());
        taskToUpdate.setStatus(Task.Status.DONE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(userId, taskId, taskToUpdate);

        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(Task.Status.DONE, result.getStatus());
    }

    @Test
    public void testDeleteTask() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();
        Task existingTask = new Task();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(userId, taskId);

        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    public void testListPaginatedTasks() {
        Long userId = 1L;
        User user = new User();
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findAllByUser(user, pageable)).thenReturn(taskPage);

        Page<Task> result = taskService.listPaginatedTasks(userId, pageable);

        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testNotFoundExceptionForCreateTask() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.createTask(userId, new Task()));
    }

    @Test
    public void testNotFoundExceptionForGetTaskById() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(userId, taskId));
    }

    @Test
    public void testNotFoundExceptionForUpdateTask() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(userId, taskId, new Task()));
    }

    @Test
    public void testNotFoundExceptionForDeleteTask() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUser(taskId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(userId, taskId));
    }

    @Test
    public void testNotFoundExceptionForListPaginatedTasks() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.listPaginatedTasks(userId, PageRequest.of(0, 10)));
    }
}
