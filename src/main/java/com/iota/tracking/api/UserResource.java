package com.iota.tracking.api;

import com.iota.tracking.domain.User;
import com.iota.tracking.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller API Endpoint
 */
@Slf4j
@RestController
@RequestMapping({"/api/iota/tracking/v1/user"})
public class UserResource {
    @Autowired
    IUserService userService;

    @GetMapping("/findUserByEmail")
    public ResponseEntity<User> findUserByEmail(
            @RequestParam("email") String email) {
        log.info("findUser {} ", email);
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping({"/findAllUsers"})
    public ResponseEntity<List<User>> findAllUsers() {
        log.info("findAllUsers");
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
