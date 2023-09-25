package com.ca.javaassessment.repositories;

import com.ca.javaassessment.entities.Task;
import com.ca.javaassessment.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndUser(Long taskId, User user);

    Page<Task> findAllByUser(User user, Pageable pageable);

    List<Task> findByStatusAndDateTimeBefore(Task.Status status, LocalDateTime dateTime);
}
