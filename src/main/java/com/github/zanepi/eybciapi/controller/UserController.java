package com.github.zanepi.eybciapi.controller;

import com.github.zanepi.eybciapi.dto.PhoneDto;
import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.service.definition.IPhoneService;
import com.github.zanepi.eybciapi.service.definition.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPhoneService phoneService;

    @PostMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody UserDto getById(@PathVariable("id") String id){
        return this.userService.getById(UUID.fromString(id));
    }

    @PostMapping(value = "/{id}/addPhone",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody PhoneDto addPhone(@PathVariable("id") String id, @RequestBody PhoneDto phoneDto){
        return this.phoneService.addPhone(phoneDto,UUID.fromString(id));
    }

}
