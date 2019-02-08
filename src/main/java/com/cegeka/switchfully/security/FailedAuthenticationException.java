package com.cegeka.switchfully.security;

import org.springframework.security.core.AuthenticationException;

public class FailedAuthenticationException extends AuthenticationException {

    public FailedAuthenticationException(String msg) {
        super(msg);
    }
}
