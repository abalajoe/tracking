package com.iota.tracking.dto;

import com.iota.tracking.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAuthenticationDTO {
    @NotEmpty(message = "The email is required.")
    private String email;
    @NotEmpty(message = "The passwprd is required.")
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    GrantedAuthority roles;
    private String token;
    private User user;
}
