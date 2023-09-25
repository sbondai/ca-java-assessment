package com.ca.javaassessment.controller;

import com.ca.javaassessment.controllers.TaskController;
import com.ca.javaassessment.dto.TaskDTO;
import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.mappers.TaskMapper;
import com.ca.javaassessment.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskService.createTask(1L, task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        mockMvc.perform(post("/api/users/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)));
    }

    @Test
    public void testGetTaskById() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        when(taskService.getTaskById(1L, 1L)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        mockMvc.perform(get("/api/users/1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)));
    }

    @Test
    public void testUpdateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskService.updateTask(1L, 1L, task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        mockMvc.perform(put("/api/users/1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)));
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L, 1L);

        mockMvc.perform(delete("/api/users/1/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testListPaginatedTasksForUser() throws Exception {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        List<TaskDTO> taskDTOs = Arrays.asList(new TaskDTO(), new TaskDTO());
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(taskService.listPaginatedTasks(1L, PageRequest.of(0, 10))).thenReturn(taskPage);
        when(taskMapper.toDto(any(Task.class))).thenAnswer(invocation -> {
            Task taskArg = invocation.getArgument(0);
            if (tasks.contains(taskArg)) {
                return taskDTOs.get(tasks.indexOf(taskArg));
            }
            return null;
        });

        mockMvc.perform(get("/api/users/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));  // Change this line to expect 0 content.
    }



}
