package com.example.Agency.service;

import com.example.Agency.model.Hotel;
import com.example.Agency.model.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();
    //void saveUser(List<User> users);
    void saveUser(User users);
}
