package com.hospital.app.auth;

import com.hospital.app.dto.RegisterDTO;
import com.hospital.app.helpers.ResponseLayout;

public interface AuthService {
    ResponseLayout<?> register(RegisterDTO registerDTO);
}
