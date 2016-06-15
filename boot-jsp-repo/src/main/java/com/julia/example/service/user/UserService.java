package com.julia.example.service.user;

import com.julia.example.domain.UserCreateForm;
import com.julia.example.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

    User update(UserCreateForm form, Long id);

    User save(User user);

    void remove(Long id);
}
