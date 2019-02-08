package com.cegeka.switchfully.security;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ArmyAuthenticationProviderIntegrationTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Authentication authenticationArgument;

    private ArmyAuthenticationProvider authenticationProvider;

    @Before
    public void setUp() throws Exception {
        authenticationProvider = new ArmyAuthenticationProvider();
    }

    @Test
    public void authenticate_givenCorrectUsernameAndPassword_ThenReturnsAuthenticationObject() {
        when(authenticationArgument.getPrincipal()).thenReturn("ZWANETTA");
        when(authenticationArgument.getCredentials()).thenReturn("WORST");

        Authentication actual = authenticationProvider.authenticate(authenticationArgument);

        assertThat(actual.isAuthenticated()).isTrue();
    }

    @Test
    public void authenticate_givenInvalidUsernameOrPassword_ThenThrowsAuthenticationException() {
        expectedException.expect(FailedAuthenticationException.class);

        when(authenticationArgument.getPrincipal()).thenReturn("ZWANETTA");
        when(authenticationArgument.getCredentials()).thenReturn("GEENWORST");

        Authentication actual = authenticationProvider.authenticate(authenticationArgument);
    }
}