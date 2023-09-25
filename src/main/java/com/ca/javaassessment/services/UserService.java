package com.ca.javaassessment.services;

import com.ca.javaassessment.dto.UserDTO;
import com.ca.javaassessment.entities.User;
import com.ca.javaassessment.exceptions.NotFoundException;
import com.ca.javaassessment.mappers.UserMapper;
import com.ca.javaassessment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO save(UserDTO userDTO) {
        userDTO.setId(null);
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDTO update(Long id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        User updatedUser = userMapper.toEntity(updatedUserDTO);
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        User savedUser = userRepository.save(existingUser);
        return userMapper.toDto(savedUser);
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }
}
