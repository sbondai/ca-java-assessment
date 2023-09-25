package com.ca.javaassessment.mappers;

import com.ca.javaassessment.dto.UserDTO;
import com.ca.javaassessment.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}

