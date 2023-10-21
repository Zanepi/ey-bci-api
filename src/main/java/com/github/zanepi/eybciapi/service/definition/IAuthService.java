package com.github.zanepi.eybciapi.service.definition;

import com.github.zanepi.eybciapi.dto.LoginCredentials;
import com.github.zanepi.eybciapi.dto.LoginResponse;
import com.github.zanepi.eybciapi.dto.UserDto;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialException;

public interface IAuthService {

    public UserDto register(UserDto userDto) throws Exception;
    public LoginResponse login(LoginCredentials credentials) throws CredentialException, AccountExpiredException;
}
