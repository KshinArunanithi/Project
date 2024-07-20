package com.bookshop.service.impl;

import com.bookshop.model.User;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.UserService;

import io.jsonwebtoken.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService	{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
   
	 
	@Autowired
    private UserRepository userRepository;

    @Override
    public User validateUser(String username) {
        logger.info("Entry point of validate user");
        try {
        logger.info("Exit point #1 of validateUser");
    	return userRepository.findByUsername(username);
        }catch(Exception e) {
        	logger.error("Error while validate user ",e);
        	logger.info("Exit point #2 of validate user");
        	return null;
        }
	
    }

    @Override
	public void save(User user) throws DataIntegrityViolationException,IOException{		
    	logger.info("Entry point of save user");
    	
    	logger.error("Exit point #1 of save user");
		userRepository.save(user);
	
    }
}
