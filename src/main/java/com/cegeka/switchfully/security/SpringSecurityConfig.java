package com.cegeka.switchfully.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
            .anyRequest().authenticated()
            .and().httpBasic()
            .authenticationEntryPoint(authEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
            .withUser("JMILLER")
            .password("{bcrypt}$2b$10$SnP8sCZzWeIWs1t713/ty.TD7.CTGOg00D5E4Hfzjurb/AhS8Vkmi").roles("USER");
    }

}
