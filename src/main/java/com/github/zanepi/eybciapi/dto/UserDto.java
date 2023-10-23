package com.github.zanepi.eybciapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class UserDto {

    UUID user_id;
    @NotNull
    String name;
    @NotNull
    @Email
    String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z].*[a-z])(?=.*\\d.*\\d).+$"
            ,message = "The password must contain: At least 1 Uppercase letter, at least 1 lowercase letter, 2 numbers")
    String password;
    @Valid
    @UniqueElements
    List<PhoneDto> phones;
    Date created;
    Date modified;
    Date last_login;
    String token;
    @Builder.Default
    boolean active = true;

}
