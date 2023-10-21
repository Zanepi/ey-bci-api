package com.github.zanepi.eybciapi.service.definition;

import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    public User convertToEntity(UserDto userDto);
    public UserDto convertToDto(User user);
    public User create(UserDto userDto) throws Exception;
    public Optional<User> findByEmail(String email);
    public UserDto getById(UUID user_id);
}
