package com.example.Agency.service;

import com.example.Agency.model.User;
import com.example.Agency.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepo userRepo;
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getUsers() {
        return this.userRepo.findAll();
    }

    @Override
    public void saveUser(User user) {
            userRepo.save(user);
    }
}
