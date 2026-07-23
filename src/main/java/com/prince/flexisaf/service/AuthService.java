package com.prince.flexisaf.service;

import com.prince.flexisaf.dto.AuthRequest;
import com.prince.flexisaf.dto.AuthResponse;
import com.prince.flexisaf.dto.UserRequest;
import com.prince.flexisaf.dto.UserResponse;
import com.prince.flexisaf.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthResponse register(UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userResponse.getEmail());
        String token = jwtService.generateToken(userDetails);
        
        return AuthResponse.builder()
                .token(token)
                .email(userResponse.getEmail())
                .role(userResponse.getRole().name())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        
        UserResponse userResponse = userService.getUserByEmail(request.getEmail());
        
        return AuthResponse.builder()
                .token(token)
                .email(userResponse.getEmail())
                .role(userResponse.getRole().name())
                .build();
    }
}
