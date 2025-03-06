package com.example.hogwarts_artifacts_online.user;

import com.example.hogwarts_artifacts_online.system.Result;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import com.example.hogwarts_artifacts_online.user.converter.UserDtoToUserConverter;
import com.example.hogwarts_artifacts_online.user.converter.UserToUserDtoConverter;
import com.example.hogwarts_artifacts_online.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private  final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @PostMapping("/api/v1/users")
    public Result addUser(@Valid @RequestBody User user){
        //save the user by encrypting the password
       User savedUser =  this.userService.saveUser(user);
        //convert the User entity to user dto for the end user
        UserDTO userDTO  = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Save One Success", userDTO);

    }

    @GetMapping("/api/v1/users")
    public Result getAllUsers(){
        List<User> usersList = this.userService.fetchAllUsers();
        //convert each user entity to dto
       List<UserDTO> usersDtosList  = usersList.stream().map(this.userToUserDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", usersDtosList);
    }

    @GetMapping("/api/v1/users/{userId}")
    public Result getUserById(@PathVariable Long userId){
        //fetch user by id from service method
        User returnedUser = this.userService.fetchUserById(userId);
        //Convert User entity to user dto
        UserDTO returnedUserDto = this.userToUserDtoConverter.convert(returnedUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", returnedUserDto);

    }

    @PutMapping("/api/v1/users/{userId}")
    public Result updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long userId){
        User updatedUser = this.userService.updateUserDetails(userId, userDTO);
        //convert user to user dto
        UserDTO updatedUserDto = this.userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update One Success", updatedUserDto);

    }

    @DeleteMapping("/api/v1/users/{userId}")
    public Result deleteUser(@PathVariable Long userId){
        this.userService.deleteUser(userId);
        //convert user to user dto
        //UserDTO deletedUserDto = this.userToUserDtoConverter.convert(deletedUser);
        return new Result(true, StatusCode.SUCCESS, "Deletion successful for user id: " + userId , null);
    }
}
