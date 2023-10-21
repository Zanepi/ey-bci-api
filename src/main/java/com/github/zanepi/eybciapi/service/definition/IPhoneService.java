package com.github.zanepi.eybciapi.service.definition;

import com.github.zanepi.eybciapi.dto.PhoneDto;
import com.github.zanepi.eybciapi.entity.Phone;
import com.github.zanepi.eybciapi.entity.User;

import java.util.List;
import java.util.UUID;

public interface IPhoneService {
    public List<Phone> create(List<PhoneDto> phoneDtoList, User user);
    public PhoneDto addPhone(PhoneDto phoneDto,UUID user_id);
}
