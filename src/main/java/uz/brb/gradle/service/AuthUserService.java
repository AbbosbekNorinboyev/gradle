package uz.brb.gradle.service;

import uz.brb.gradle.dto.request.LoginRequest;
import uz.brb.gradle.dto.request.RegisterRequest;
import uz.brb.gradle.dto.request.UpdatePasswordRequest;
import uz.brb.gradle.dto.response.Response;

public interface AuthUserService {
    Response<?> register(RegisterRequest registerRequest);

    Response<?> login(LoginRequest loginRequest);

    Response<?> changePassword(UpdatePasswordRequest request);
}