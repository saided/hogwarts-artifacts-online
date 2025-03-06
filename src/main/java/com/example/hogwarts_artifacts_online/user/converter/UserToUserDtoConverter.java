package com.example.hogwarts_artifacts_online.user.converter;

import com.example.hogwarts_artifacts_online.user.User;
import com.example.hogwarts_artifacts_online.user.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO(source.getId(), source.getUsername(), source.getRoles(), source.getEnabled());
        return userDTO;

    }
}
