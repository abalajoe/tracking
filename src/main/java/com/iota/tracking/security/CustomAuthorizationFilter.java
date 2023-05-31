package com.iota.tracking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.iota.tracking.configuration.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Intercept every request and check authorization
 */
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/adam/client/refreshtoken")) {
            filterChain.doFilter(request, response); // if permitted url pass request to next filter in filter chain
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length()); // remove 'Bearer ' and get token
                Algorithm algorithm = Algorithm.HMAC256(AppConfig.SECRET_KEY.getBytes()); // define algorithm; use same secret used to sign algorithm
                JWTVerifier verifier = JWT.require(algorithm).build(); //verify token
                DecodedJWT decodedJWT = verifier.verify(token); // verify and get decoded jwt, if fails it will be caught in catch block
                String username = decodedJWT.getSubject(); // token valid, get username
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class); // look at roles in json web token and grab them;
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); // convert roles as string to SimpleGrantedAuthority as that is what spring security is expecting
                stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities); // we dont need credentials (password) because at this point user is already authenticated and json web token is valid so set them in authentication context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
