package com.ca.javaassessment.service;

import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.entities.User;
import com.ca.javaassessment.repositories.TaskRepository;
import com.ca.javaassessment.services.schedule.TaskSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TaskSchedulerServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskSchedulerService taskSchedulerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskSchedulerService = new TaskSchedulerService(taskRepository);
    }

    @Test
    public void testCheckAndProcessTasks() {
        List<Task> pendingTasks = generateMockTasks(5000);

        when(taskRepository.findByStatusAndDateTimeBefore(eq(Task.Status.PENDING), any(LocalDateTime.class)))
                .thenReturn(pendingTasks);

        taskSchedulerService.processPendingTasks();

        verify(taskRepository, times(1)).findByStatusAndDateTimeBefore(eq(Task.Status.PENDING), any(LocalDateTime.class));
        verify(taskRepository, times(1)).saveAll(pendingTasks);
    }
    private List<Task> generateMockTasks(int count) {
        List<Task> tasks = new ArrayList<>();
        Random random = new Random();
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("mockUser");
        mockUser.setFirstName("Mock");
        mockUser.setLastName("User");

        for (int i = 0; i < count; i++) {
            Task task = new Task();
            task.setId((long) i);
            task.setDateTime(LocalDateTime.now().minusDays(random.nextInt(10)));
            task.setStatus(Task.Status.PENDING);
            task.setUser(mockUser);
            tasks.add(task);
        }

        return tasks;
    }
}
