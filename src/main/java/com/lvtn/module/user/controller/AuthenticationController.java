package com.lvtn.module.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvtn.module.shared.dto.UserChangePasswordDto;
import com.lvtn.module.shared.security.JWTTokenHelper;
import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.module.user.dto.AuthenticationRequest;
import com.lvtn.module.user.dto.AuthenticationResponse;
import com.lvtn.module.shared.model.User;
import com.lvtn.module.user.mapper.UserMapper;
import com.lvtn.module.user.service.UserService;
import com.lvtn.platform.common.ApiMessage;
import com.lvtn.platform.common.Response;
import com.lvtn.platform.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTTokenHelper jwtTokenHelper;
    private final UserMapper mapper;
    private final UserService userService;

    @Value("${jwt.auth.access_expires_in}")
    private int accessExpiresIn;

    @Value("${jwt.auth.refresh_expires_in}")
    private int refreshExpiresIn;

    @PostMapping("/auth/login")
    public Response<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwtAccessToken = jwtTokenHelper.generateToken(user,accessExpiresIn);
            String jwtRefreshToken = jwtTokenHelper.generateToken(user,refreshExpiresIn);

            AuthenticationResponse response = new AuthenticationResponse();
            response.setAccessToken(jwtAccessToken);
            response.setRefreshToken(jwtRefreshToken);
            response.setUserDtoResponse(mapper.mapUserToUserDtoResponse(user));

            return Response.success(response);
        }
        catch (Exception e) {
            return Response.error(111111,"Tên đăng nhập hoặc mật khẩu không chính xác");
        }
    }

    @PostMapping("/refresh_token")
    public Response<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        String authToken = jwtTokenHelper.getToken(request);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if (authToken != null) {
            try {
                String userName = jwtTokenHelper.getUsernameFromToken(authToken);
                if (userName != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    if (jwtTokenHelper.validateToken(authToken, userDetails)) {
                        User user = userService.getUser(userName);
                        String jwtAccessToken = jwtTokenHelper.generateToken(user,accessExpiresIn);
                        String jwtRefreshToken = jwtTokenHelper.generateToken(user,refreshExpiresIn);
                        authenticationResponse.setRefreshToken(jwtRefreshToken);
                        authenticationResponse.setAccessToken(jwtAccessToken);
                        return Response.success(authenticationResponse);
                    }
                }
                return Response.error(123123,"User không hợp lệ");

            } catch (Exception e) {
                return Response.error(123123,"Refresh token không hợp lệ");
            }
        }
        else {
            return Response.error(123123,"Không tìm thấy refresh token");
        }
    }

    @PostMapping("/user/change_password")
    public Response<AuthenticationResponse> changePassword(@Valid @RequestBody UserChangePasswordDto userChangePasswordDto, Principal user) {
        try {
            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), userChangePasswordDto.getOldPassword()));

            } catch (Exception e) {
                  return Response.error(ApiClientMessage.WRONG_PASSWORD.getCode(),ApiClientMessage.WRONG_PASSWORD.getMessage());
            }
            User new_user = userService.changePassword(userChangePasswordDto,user);
            String jwtAccessToken = jwtTokenHelper.generateToken(new_user,accessExpiresIn);
            String jwtRefreshToken = jwtTokenHelper.generateToken(new_user,refreshExpiresIn);
            AuthenticationResponse response = new AuthenticationResponse();
            response.setAccessToken(jwtAccessToken);
            response.setRefreshToken(jwtRefreshToken);
            response.setUserDtoResponse(mapper.mapUserToUserDtoResponse(new_user));
            return Response.success(response);
        }
        catch (ApiException e) {
            return Response.error(e.getCode(),e.getMessage());
        }
    }
}
