package com.example.hogwarts_artifacts_online.user;

import com.example.hogwarts_artifacts_online.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    public User fetchUserById(Long userId){
        return this.userRepository.findById(userId).orElseThrow(()->
            new UserNotFoundException(userId)
        );

    }

    public List<User> fetchAllUsers(){
        return  this.userRepository.findAll();
    }

    public User updateUserDetails(Long userId, UserDTO userDto){
        //first find if the user with the userid exists or not
        User returnedUser = this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        returnedUser.setUsername(userDto.username() != null ? userDto.username() : returnedUser.getUsername());
        returnedUser.setRoles(userDto.roles() != null ? userDto.roles() : returnedUser.getRoles());
        returnedUser.setEnabled(userDto.enabled() != returnedUser.getEnabled() ? userDto.enabled() : returnedUser.getEnabled());
        return this.userRepository.save(returnedUser);

    }

    public User deleteUser(Long userId){
        User userToDel = this.userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
        return userToDel;
    }
}
