package com.hospital.app.dto.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AccountantDentistCreateRequest(
        @NotNull
        @NotEmpty
        Long userId,
        @NotNull
        @NotEmpty
        @Length(min = 10, max = 10)
        @Pattern(regexp = "^(0[1-9]{1}[0-9]{8}|(84|0)[1-9]{1}[0-9]{8})$\n")
        String phoneNumber,
        @NotNull
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$\n")
        String email,
        Long specializeId
) {
}
