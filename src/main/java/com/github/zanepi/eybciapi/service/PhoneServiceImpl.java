package com.github.zanepi.eybciapi.service;

import com.github.zanepi.eybciapi.dto.PhoneDto;
import com.github.zanepi.eybciapi.entity.Phone;
import com.github.zanepi.eybciapi.entity.User;
import com.github.zanepi.eybciapi.repository.PhoneRepository;
import com.github.zanepi.eybciapi.repository.UserRepository;
import com.github.zanepi.eybciapi.service.definition.IPhoneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhoneServiceImpl implements IPhoneService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Phone> create(List<PhoneDto> phoneDtoList, User user) {
        List<Phone> createdPhones = phoneDtoList
                .stream()
                .map((p)->this.modelMapper.map(p,Phone.class))
                .toList();
        createdPhones.forEach((phone -> phone.setUser(user)));

        return this.phoneRepository.saveAll(createdPhones);
    }

    @Override
    public PhoneDto addPhone(PhoneDto phoneDto, UUID user_id) {
        Optional<User> user = this.userRepository.findById(user_id);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User with the provided ID not found");
        }

        List<Phone> createdPhone = create(Collections.singletonList(phoneDto),user.get());

        return this.modelMapper.map(createdPhone.get(0),PhoneDto.class);

    }
}
