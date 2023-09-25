package com.ca.javaassessment.mappers;

import com.ca.javaassessment.dto.TaskDTO;
import com.ca.javaassessment.entities.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);
}

