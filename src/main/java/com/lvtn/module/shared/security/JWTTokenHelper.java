package com.lvtn.module.shared.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.model.User;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTTokenHelper {
    @Value("${jwt.auth.app}")
    private String appName;

    @Value("${jwt.auth.secret_key}")
    private String secretKey;


    public String getUsernameFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public String generateToken(User user, int expiresIn) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

        return JWT.create().withSubject(user.getUsername()).withExpiresAt(generateExpirationDate(expiresIn)).withClaim("role",user.getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority()).withIssuer(appName).sign(algorithm);
    }

    private Date generateExpirationDate(int expiresIn) {
        return new Date(new Date().getTime() + expiresIn * 1000);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        final String username = decodedJWT.getSubject();

        Date expireDate = decodedJWT.getExpiresAt();

        if (username == null || !username.equals(userDetails.getUsername())) {
            throw new ApiException(ApiSharedMesssage.WRONG_PASSWORD);
        }
        if (expireDate.before(new Date())) {
            throw new ApiException(ApiSharedMesssage.TOKEN_EXPIRED);
        }
        return true;
    }

    public String getToken( HttpServletRequest request ) {

        String authHeader = getAuthHeaderFromHeader( request );
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getAuthHeaderFromHeader( HttpServletRequest request ) {
        return request.getHeader("Authorization");
    }
}
