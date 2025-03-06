package com.example.hogwarts_artifacts_online.user.converter;

import com.example.hogwarts_artifacts_online.user.User;
import com.example.hogwarts_artifacts_online.user.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setId(source.id());
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());

        return user;
    }
}
