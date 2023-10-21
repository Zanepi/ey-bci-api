package com.github.zanepi.eybciapi.service;

import com.github.zanepi.eybciapi.dto.LoginCredentials;
import com.github.zanepi.eybciapi.dto.LoginResponse;
import com.github.zanepi.eybciapi.dto.PhoneDto;
import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.entity.Phone;
import com.github.zanepi.eybciapi.entity.User;
import com.github.zanepi.eybciapi.service.definition.IAuthService;
import com.github.zanepi.eybciapi.service.definition.IPhoneService;
import com.github.zanepi.eybciapi.service.definition.IUserService;
import com.github.zanepi.eybciapi.utils.auth.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPhoneService phoneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) throws Exception {

        Optional<User> user = this.userService.findByEmail(userDto.getEmail());
        if(user.isPresent()) {
            throw new UsernameNotFoundException("The email is already registered");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User createdUser = this.userService.create(userDto);

        List<Phone> createdUserPhones = this.phoneService.create(userDto.getPhones(),createdUser);
        createdUser.setPhones(createdUserPhones);
        return this.userService.convertToDto(createdUser);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginCredentials credentials) throws UsernameNotFoundException, AccountExpiredException {

        Optional<User> user = this.userService.findByEmail(credentials.getEmail());
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("There isn't a user registered with the email provided");
        }
        if(!user.get().isActive()){
            throw new AccountExpiredException("The user registered with the provided email is no longer active");
        }
        if(!passwordEncoder.matches(credentials.getPassword(),user.get().getPassword())){
            throw new BadCredentialsException("The password is incorrect");
        }

        String accessToken = generateToken(user.get());

        user.get().setLast_login(new Date());
        user.get().setToken(accessToken);

        return new LoginResponse(credentials.getEmail(),accessToken);
    }

    private String generateToken(User user){
        return this.jwtUtil.generateToken(user);
    }

}
