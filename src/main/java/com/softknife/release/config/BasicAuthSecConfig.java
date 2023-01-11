package com.softknife.release.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author alexander matsaylo on 2/23/22
 * @project release-notes
 */

@Configuration
@EnableWebSecurity
public class BasicAuthSecConfig {

    private final BasicAuthEntry authenticationEntryPoint;
    private final SecurityProp securityProp;
    private static final String[] ACTUATOR_WHITELIST = {"/actuator/**", "/v3/api-docs"};

    public BasicAuthSecConfig(BasicAuthEntry authenticationEntryPoint, SecurityProp securityProp) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.securityProp = securityProp;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ACTUATOR_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User
                .withUsername(securityProp.getUser())
                .password(securityProp.getPassword())
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}