package com.ca.javaassessment.service;

import com.ca.javaassessment.dto.UserDTO;
import com.ca.javaassessment.entities.User;
import com.ca.javaassessment.exceptions.NotFoundException;
import com.ca.javaassessment.mappers.UserMapper;
import com.ca.javaassessment.repositories.UserRepository;
import com.ca.javaassessment.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.save(userDTO);

        assertEquals(userDTO, result);
    }

    @Test
    public void testFindById() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(1L);

        assertEquals(userDTO, result);
    }

    @Test
    public void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    public void testUpdate() {
        UserDTO updatedUserDTO = new UserDTO();
        User existingUser = new User();
        User updatedUser = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userMapper.toEntity(updatedUserDTO)).thenReturn(updatedUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(updatedUserDTO);

        UserDTO result = userService.update(1L, updatedUserDTO);

        assertEquals(updatedUserDTO, result);
    }

    @Test
    public void testUpdate_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(1L, new UserDTO()));
    }

    @Test
    public void testDeleteById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteById(1L));
    }

    @Test
    public void testFindAll() {
        List<User> users = Arrays.asList(new User(), new User());
        List<UserDTO> userDTOs = Arrays.asList(new UserDTO(), new UserDTO());
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toDto(any(User.class))).thenReturn(userDTOs.get(0), userDTOs.get(1));

        Page<UserDTO> resultPage = userService.findAll(Pageable.unpaged());

        assertEquals(userDTOs.size(), resultPage.getContent().size());
        assertTrue(resultPage.getContent().containsAll(userDTOs));
    }
}
