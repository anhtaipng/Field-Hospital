package com.lvtn.module.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService, JWTTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/user/send_otp")||request.getServletPath().equals("/api/user/verify_otp")||request.getServletPath().equals("/api/admin/signup")||request.getServletPath().equals("/api/auth/login")||request.getServletPath().equals("/api/patient/signup")||request.getServletPath().equals("/api/patient/signup_otp")||request.getServletPath().equals("/refresh_token")||request.getServletPath().equals("/api/transfer")) {
            filterChain.doFilter(request,response);
        } else {
                String authToken = jwtTokenHelper.getToken(request);
                if (authToken != null) {
                    try {
                        String userName = jwtTokenHelper.getUsernameFromToken(authToken);
                        if (userName != null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                            if (jwtTokenHelper.validateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authentication.setDetails(new WebAuthenticationDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                filterChain.doFilter(request,response);
                            }
                        }
                    } catch (Exception e) {
                        Map<String, Object> error = new HashMap<>();
                        error.put("success", false);
                        error.put("code", 123123);
                        error.put("message", e.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
                }
                else {
                    Map<String,Object> error = new HashMap<>();
                    error.put("success",false);
                    error.put("code",123456);
                    error.put("message","Không tìm thấy access token");
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
        }
    }
}
