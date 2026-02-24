package com.P2.warhammer.users;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addUser(String name, int age) {
        return repository.save(new User(name, age));

    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
