package com.lys.wheeling.service.authentication;

import com.lys.wheeling.domain.User;

public interface AuthenticationService {
    public User signInAndReturnJWT(User signInRequest);
}
