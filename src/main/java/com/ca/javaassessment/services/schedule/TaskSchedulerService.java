package com.ca.javaassessment.services.schedule;

import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TaskSchedulerService {

    private final TaskRepository taskRepository;

    public TaskSchedulerService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void processPendingTasks() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Task> pendingTasks = taskRepository.findByStatusAndDateTimeBefore(Task.Status.PENDING, now);

            if (pendingTasks != null && !pendingTasks.isEmpty()) {
                pendingTasks.forEach(task -> {
                    log.info("Processing Task: {}", task.getId());
                    task.setStatus(Task.Status.DONE);
                });

                taskRepository.saveAll(pendingTasks);
            }
        } catch (org.springframework.dao.InvalidDataAccessResourceUsageException e) {
             log.error("No record found");
        } catch (Exception e) {
            log.error("Error processing tasks: ", e);
        }
    }


    @Scheduled(cron = "0 */1 * * * *")
    public void scheduledTaskProcessing() {
        processPendingTasks();
    }
}
