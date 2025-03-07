package edu.alexandra.pet.service;

import edu.alexandra.pet.controller.request.user.LoginRequest;
import edu.alexandra.pet.controller.request.user.RegisterRequest;
import edu.alexandra.pet.controller.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequest registerRequest);
}