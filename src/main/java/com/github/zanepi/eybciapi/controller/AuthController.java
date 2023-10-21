package com.github.zanepi.eybciapi.controller;

import com.github.zanepi.eybciapi.dto.LoginCredentials;
import com.github.zanepi.eybciapi.dto.LoginResponse;
import com.github.zanepi.eybciapi.dto.StringResponse;
import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.service.definition.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialException;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @GetMapping(value = "/ping",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody StringResponse ping(){
        return new StringResponse("You have access to the Auth Controller");
    }

    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody UserDto register(@Valid @RequestBody UserDto userDto) throws Exception{
        return this.authService.register(userDto);
    }

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody LoginResponse login(@Valid @RequestBody LoginCredentials credentials) throws CredentialException, AccountExpiredException {
        return this.authService.login(credentials);
    }

}
