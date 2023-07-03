package com.coderdot.services;

import com.coderdot.dto.SignupRequest;
import com.coderdot.entities.Customer;

public interface AuthService {
    Customer createCustomer(SignupRequest signupRequest);
}
