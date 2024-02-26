package com.example.Agency.service;

import com.example.Agency.model.User;
import java.util.List;

public interface IUserService {
    List<User> getUsers();
    void saveUser(User users);
}
