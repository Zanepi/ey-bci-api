package com.github.zanepi.eybciapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneDto {

    @NotNull
    String countrycode;
    @NotNull
    String citycode;
    @NotNull
    String number;
}
