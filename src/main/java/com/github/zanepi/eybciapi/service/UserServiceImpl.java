package com.github.zanepi.eybciapi.service;

import com.github.zanepi.eybciapi.dto.PhoneDto;
import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.entity.Phone;
import com.github.zanepi.eybciapi.entity.User;
import com.github.zanepi.eybciapi.repository.PhoneRepository;
import com.github.zanepi.eybciapi.repository.UserRepository;
import com.github.zanepi.eybciapi.service.definition.IUserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PhoneRepository phoneRepository;

    private ModelMapper modelMapper;
    private TypeMap<User,UserDto> dtoMapper;
    private TypeMap<UserDto,User> entityMapper;

    public UserServiceImpl(){
        this.modelMapper = new ModelMapper();
        this.dtoMapper = this.modelMapper.createTypeMap(User.class, UserDto.class);
        this.entityMapper = this.modelMapper.createTypeMap(UserDto.class, User.class);
        this.entityMapper.addMappings(mapper -> mapper.skip(User::setPhones));
    }

    @Override
    public User convertToEntity(UserDto userDto) {return entityMapper.map(userDto);}

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private List<Phone> convertPhonesToEntity(List<PhoneDto> phoneDtos){
        return phoneDtos.stream().map((p)->modelMapper.map(p,Phone.class)).toList();
    }

    @Override
    @Transactional
    public User create(UserDto userDto) throws Exception {
        User user = this.convertToEntity(userDto);
//        List<Phone> userPhones = convertPhonesToEntity(userDto.getPhones());
//        userPhones.forEach((p) -> p.setUser(user));

        this.userRepository.save(user);

        if(user.getUser_id() == null){
            throw new Exception("User couldn't be saved");
        }

//        user.setPhones(userPhones);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDto getById(UUID user_id) {

        Optional<User> user = this.userRepository.findById(user_id);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User with the provided ID not found");
        }

        UserDto userDto = this.modelMapper.map(user.get(),UserDto.class);
        Optional<List<Phone>> userPhones = this.phoneRepository.getPhonesByUser(user.get());

        userPhones.ifPresent(phones -> userDto.setPhones(phones.stream().map((p) -> this.modelMapper.map(p, PhoneDto.class)).toList()));

        return userDto;
    }
}
