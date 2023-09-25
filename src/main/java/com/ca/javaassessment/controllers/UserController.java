package com.ca.javaassessment.controllers;

import com.ca.javaassessment.dto.UserDTO;
import com.ca.javaassessment.services.UserService;
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
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name="User Management System", description="Operations pertaining to user in User Management System")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created user"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUserDTO = userService.save(userDTO);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a user by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update a user by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
        UserDTO savedUserDTO = userService.update(id, updatedUserDTO);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete a user by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get all users with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping
    public ResponseEntity<Page<UserDTO>> listAllUsers(
            @Parameter(description = "Results page you want to retrieve (0..N)", required = false, example = "0")
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,

            @Parameter(description = "Number of records per page.", required = false, example = "50")
            @RequestParam(name = "size", required = false, defaultValue = "50") Integer size,

            @Parameter(description = "Sorting criteria. Default is by 'firstName' in ascending order.", required = false, example = "firstName")
            @RequestParam(name = "sort", required = false, defaultValue = "firstName") String sort,

            @Parameter(hidden = true)
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, direction, sort);
        Page<UserDTO> userDTOs = userService.findAll(pageable);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

}
