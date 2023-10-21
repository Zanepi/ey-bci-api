package com.github.zanepi.eybciapi.service.definition;

import com.github.zanepi.eybciapi.dto.LoginCredentials;
import com.github.zanepi.eybciapi.dto.LoginResponse;
import com.github.zanepi.eybciapi.dto.UserDto;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialException;

public interface IAuthService {

    UserDto register(UserDto userDto) throws Exception;
    LoginResponse login(LoginCredentials credentials) throws AccountExpiredException;
}
