package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserResult;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginResult;

public interface AuthService {

    RegisterUserResult registerUser(RegisterUserRequest request);

    LoginResult login(LoginRequest request);

}
