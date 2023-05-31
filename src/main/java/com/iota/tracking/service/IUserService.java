package com.iota.tracking.service;

import com.iota.tracking.domain.User;

import java.util.List;

public interface IUserService {
    User findUserByEmail(String email);
    List<User> findAllUsers();
    User save(String name, String email, String roles);
}
