package com.example.hogwarts_artifacts_online.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId){
        super("Could not find user with user id: " + userId.toString());
    }
}
