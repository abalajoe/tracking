//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.iota.tracking.security;

import com.iota.tracking.domain.User;
import com.iota.tracking.exception.EntityNotFoundException;
import com.iota.tracking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository clientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.clientRepository.findByEmail(s);
        if (user == null) {
            log.error("user not found {}", s);
            throw new EntityNotFoundException("user not found");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList();
            String[] roles = user.getRoles().split(",");
            for (String role : roles){
                authorities.add(new SimpleGrantedAuthority(role.trim()));
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
    }
}
