package dev.meawmere.taskflow.controller;

import dev.meawmere.taskflow.common.ApiResponse;
import dev.meawmere.taskflow.exception.UsernameAlreadyExistsException;
import dev.meawmere.taskflow.payload.SigninRequest;
import dev.meawmere.taskflow.payload.SigninResponse;
import dev.meawmere.taskflow.payload.SignupRequest;
import dev.meawmere.taskflow.model.UserAccount;
import dev.meawmere.taskflow.repository.UserRepository;
import dev.meawmere.taskflow.security.JwtCore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request) throws UsernameAlreadyExistsException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists!");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        UserAccount account = new UserAccount();
        account.setUsername(request.getUsername());
        account.setPassword(hashed);

        userRepository.save(account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>success("User successfully created", null));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> signin(@Valid @RequestBody SigninRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<SigninResponse>error("Invalid username or password"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);

        SigninResponse response = SigninResponse.builder().jwt(jwt).build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
