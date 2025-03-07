package edu.alexandra.pet.controller;

import edu.alexandra.pet.controller.request.user.LoginRequest;
import edu.alexandra.pet.controller.request.user.RegisterRequest;
import edu.alexandra.pet.controller.response.AuthResponse;
import edu.alexandra.pet.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {

        log.info("Registering user with username: {}", registerRequest.getUsername());

        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        log.info("Logging in user with username: {}", loginRequest.getUsername());

        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public String logout() {

        log.info("Logging out user");

        return "logout from public endpoint";
    }
}