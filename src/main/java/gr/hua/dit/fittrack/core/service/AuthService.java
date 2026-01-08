package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginResult;

public interface AuthService {
    void registerUser(RegisterUserRequest request);

    // Προσθέστε αυτή τη γραμμή
    LoginResult login(LoginRequest request);



}