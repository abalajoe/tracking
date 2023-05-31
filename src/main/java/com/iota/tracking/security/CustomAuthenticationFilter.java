package com.iota.tracking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.iota.tracking.configuration.AppConfig;
import com.iota.tracking.dto.ClientAuthenticationDTO;
import com.iota.tracking.dto.ClientAuthenticationResponseDTO;
import com.iota.tracking.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iota.tracking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /*
     * class used to convert json string to pojo
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager; // used to authenticate user
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ClientAuthenticationDTO authenticationDTO = getAuthenticationDTO(request);
        String username = authenticationDTO.getEmail().trim();
        String password = authenticationDTO.getPassword().trim();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken); // authenticate user
    }

    // user authenticated successful
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User)authentication.getPrincipal(); // returns user successfully authenticated
        Algorithm algorithm = Algorithm.HMAC256(AppConfig.SECRET_KEY.getBytes()); // get algorithm to sign access and refresh token; in prod encrypt and decrypt secret
        String access_token = JWT.create()
                .withSubject(user.getUsername()) // unique item about user so that you can identify user i.e id, email
                .withExpiresAt(new Date(System.currentTimeMillis() + 1200000000)) // test minutes to avoid retiming
                .withIssuer(request.getRequestURL().toString()) // url of our application
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())) // list of roles
                .sign(algorithm);

        List<GrantedAuthority> l = new ArrayList<>(user.getAuthorities());
        ClientAuthenticationResponseDTO clientAuthenticationResponseDTO = ClientAuthenticationResponseDTO.
                builder().
                email(user.getUsername()).
                token(access_token).
                roles(l).
                build();

        log.info("authResponse {}", clientAuthenticationResponseDTO);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), clientAuthenticationResponseDTO);
    }

    private ClientAuthenticationDTO getAuthenticationDTO(HttpServletRequest request)  {
        String body = null;
        ClientAuthenticationDTO authenticationDTO = null;
        try {
            body = request.getReader().lines().collect(Collectors.joining());
            authenticationDTO = objectMapper.readValue(body, ClientAuthenticationDTO.class);
        } catch (IOException e) {
            log.error("ClientAuthenticationDTO {}", e.getMessage());
            throw new BadRequestException("please enter username and password");
        }

        return authenticationDTO;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("failed");
        Map<String, String> res = new HashMap<>();
        res.put("error_message", "failed authentication");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        new ObjectMapper().writeValue(response.getOutputStream(), res);
    }
}
