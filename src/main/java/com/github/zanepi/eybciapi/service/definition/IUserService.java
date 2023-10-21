package com.github.zanepi.eybciapi.service.definition;

import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    User convertToEntity(UserDto userDto);
    UserDto convertToDto(User user);
    User create(UserDto userDto) throws Exception;
    Optional<User> findByEmail(String email);
    UserDto getById(UUID user_id);
}
