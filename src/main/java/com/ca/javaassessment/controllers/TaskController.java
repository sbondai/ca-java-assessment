package com.ca.javaassessment.controllers;

import com.ca.javaassessment.dto.TaskDTO;
import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.mappers.TaskMapper;
import com.ca.javaassessment.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/tasks")
@AllArgsConstructor
@Tag(name = "Task Management", description = "Operations pertaining to tasks for users")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(summary = "Add a new Task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created Task"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@PathVariable Long userId, @RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskService.createTask(userId, task);
        return new ResponseEntity<>(taskMapper.toDto(savedTask), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a Task by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Task"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long userId, @PathVariable Long taskId) {
        Task task = taskService.getTaskById(userId, taskId);
        return new ResponseEntity<>(taskMapper.toDto(task), HttpStatus.OK);
    }

    @Operation(summary = "Update a Task by Task ID and User ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated tasks"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDTO updatedTaskDTO) {
        Task taskToUpdate = taskMapper.toEntity(updatedTaskDTO);
        Task updatedTask = taskService.updateTask(userId, taskId, taskToUpdate);
        return new ResponseEntity<>(taskMapper.toDto(updatedTask), HttpStatus.OK);
    }

    @Operation(summary = "Delete a Task by User ID and by Task Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long userId, @PathVariable Long taskId) {
        taskService.deleteTask(userId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Page<TaskDTO>> listPaginatedTasksForUser(
            @PathVariable Long userId,
            @Parameter(description = "Results page you want to retrieve (0..N)", example = "0")
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,

            @Parameter(description = "Number of records per page.", example = "50")
            @RequestParam(name = "size", required = false, defaultValue = "50") Integer size,

            @Parameter(description = "Sorting criteria. Default is by 'dateTime' you can sort by name, description or status", required = false, example = "dateTime")
            @RequestParam(name = "sort", required = false, defaultValue = "dateTime") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Task> tasks = taskService.listPaginatedTasks(userId, pageable);
        if (tasks == null) {
            return ResponseEntity.ok(Page.empty());
        }
        Page<TaskDTO> taskDTOs = tasks.map(taskMapper::toDto);
        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

}
