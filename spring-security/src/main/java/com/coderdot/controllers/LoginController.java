package com.coderdot.controllers;

import com.coderdot.dto.LoginRequest;
import com.coderdot.dto.LoginResponse;
import com.coderdot.services.jwt.CustomerServiceImpl;
import com.coderdot.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final CustomerServiceImpl customerService;

    private final JwtUtil jwtUtil;


    @Autowired
    public LoginController(AuthenticationManager authenticationManager, CustomerServiceImpl customerService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer is not activated");
            return null;
        }
        final UserDetails userDetails = customerService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new LoginResponse(jwt);
    }

}
