package com.como.service.receiveapprequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Map<String, User> users = new HashMap<>();

    public UserService(){
        User jolene = new User();
        jolene.setUsername("jolene22");
        jolene.setPassword("hello");
        jolene.setEmail("jolene@como.com");
        jolene.setRedemptionPoints(10000);
        users.put(jolene.getUsername(), jolene);
    }

    public List<User> getAllUsers() {
		return new ArrayList(users.values());
	}

	public User getUser(String username) {
		return users.get(username);
	}

	public void addUser(User user) {
	 	users.put(user.getUsername(), user);
	}

    public boolean checkUsername(String username){
        if(users.get(username) == null){
            return false;
        }
        return true;
    }

    public boolean checkPassword(String username, String password){
        if(users.get(username) == null) {
            return false;
        } else if((getUser(username)).getPassword().equals(password)){
            return true;
        }
        return false;
    }
}
