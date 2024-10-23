package com.hospital.app.dto.auth;

import com.hospital.app.entities.account.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotNull
        @NotEmpty
        String username,
        @NotNull
        @NotEmpty
        @Length(min = 8, max = 20)
        String password,
        @NotNull
        @NotEmpty
        String fullName,
        @NotNull
        @NotEmpty
        Gender gender,
        @NotNull
        @NotEmpty
        @Length(min = 10, max = 10)
        @Pattern(regexp = "^(0[1-9]{1}[0-9]{8}|(84|0)[1-9]{1}[0-9]{8})$\n")
        String phoneNumber,
        @NotNull
        @NotEmpty
        String address,
        @NotNull
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$\n")
        String email
) {
}
