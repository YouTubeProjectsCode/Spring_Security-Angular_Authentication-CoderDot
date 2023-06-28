package com.coderdot.dto;

public class LoginResponse {

    private String jwtToken;

    public LoginResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
