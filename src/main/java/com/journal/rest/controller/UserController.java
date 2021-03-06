package com.journal.rest.controller;

import com.journal.data.dto.CurrentUserDto;
import com.journal.data.dto.UpdateUserDto;
import com.journal.data.dto.UserDto;
import com.journal.rest.BaseResponse;
import com.journal.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
@PreAuthorize("hasAnyAuthority('ADMIN','MONITOR','STUDENT')")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Returns all available users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all users"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public BaseResponse<List<UserDto>> findAllUsers(@RequestParam(required = false, defaultValue = "ua") String lang) {
        return new BaseResponse<>(userService.findAll(lang), HttpStatus.OK.value());
    }

    @ApiOperation("Update one user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update one user"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MONITOR')")
    public BaseResponse<Void> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto request) {
        userService.update(request, id);
        return new BaseResponse<>();
    }

    @GetMapping("/current")
    public BaseResponse<CurrentUserDto> getCurrentUser(@RequestParam(required = false, defaultValue = "ua") String lang) {

        return new BaseResponse<>(userService.getCurrentUser(lang), HttpStatus.OK.value());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}