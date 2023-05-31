package com.iota.tracking.dto;

import com.iota.tracking.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAuthenticationResponseDTO {
    private String token;
    private String email;
    private List<GrantedAuthority> roles;
}
