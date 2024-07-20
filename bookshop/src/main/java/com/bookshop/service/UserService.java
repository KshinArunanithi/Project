package com.bookshop.service;

import com.bookshop.model.User;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

     public User validateUser(String userId);
     
     public void save(User user);
}
