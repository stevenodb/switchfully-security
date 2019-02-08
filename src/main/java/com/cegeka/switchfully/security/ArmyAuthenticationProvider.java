package com.cegeka.switchfully.security;

import com.cegeka.switchfully.security.external.authentication.ExternalAuthenticaton;
import com.cegeka.switchfully.security.external.authentication.FakeAuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ArmyAuthenticationProvider implements AuthenticationProvider {

    private FakeAuthenticationService authenticationService = new FakeAuthenticationService();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        ExternalAuthenticaton externalAuthenticaton =
            authenticationService.getUser(
                (String) authentication.getPrincipal(), (String) authentication.getCredentials());

        if (nonNull(externalAuthenticaton)) {
            Set<SimpleGrantedAuthority> roles = externalAuthenticaton.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

            return new UsernamePasswordAuthenticationToken(
                externalAuthenticaton.getUsername(),
                externalAuthenticaton.getPassword(),
                roles);
        }
        throw new FailedAuthenticationException("Username or password invalid or unknown.");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
