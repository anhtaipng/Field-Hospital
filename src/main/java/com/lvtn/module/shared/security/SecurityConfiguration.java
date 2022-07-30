package com.lvtn.module.shared.security;

import com.lvtn.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JWTTokenHelper jwtTokenHelper;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
        http.authorizeRequests((request)->request.antMatchers("/api/user/send_otp","/api/user/verify_otp","/api/admin/signup","/api/auth/login", "/api/patient/signup","/api/patient/signup_otp","/api/refresh_token","/api/user/profile", "/api/transfer").permitAll());
        http.authorizeRequests((request)->request.antMatchers("/api/patient/**").hasAnyAuthority("ROLE_PATIENT"));
        http.authorizeRequests((request)->request.antMatchers("/api/doctor/**").hasAnyAuthority("ROLE_DOCTOR"));
        http.authorizeRequests((request)->request.antMatchers("/api/nurse/**").hasAnyAuthority("ROLE_NURSE"));
        http.authorizeRequests((request)->request.antMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN"));
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new JWTAuthenticationFilter(userService, jwtTokenHelper), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable().cors().and().headers().frameOptions().disable();
    }
}
