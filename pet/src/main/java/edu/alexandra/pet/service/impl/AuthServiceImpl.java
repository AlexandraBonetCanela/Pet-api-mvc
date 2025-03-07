package edu.alexandra.pet.service.impl;

import edu.alexandra.pet.controller.request.user.LoginRequest;
import edu.alexandra.pet.controller.request.user.RegisterRequest;
import edu.alexandra.pet.controller.response.AuthResponse;
import edu.alexandra.pet.model.user.User;
import edu.alexandra.pet.repository.UserRepository;
import edu.alexandra.pet.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.getToken(user);

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {

        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
        String token = jwtService.getToken(user);

        return new AuthResponse(token);
    }
}
