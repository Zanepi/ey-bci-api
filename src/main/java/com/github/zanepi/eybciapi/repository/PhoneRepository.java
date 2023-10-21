package com.github.zanepi.eybciapi.repository;

import com.github.zanepi.eybciapi.entity.Phone;
import com.github.zanepi.eybciapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {
    public Optional<List<Phone>> getPhonesByUser(User user);
}
