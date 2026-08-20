package com.kodbank;

import java.util.*;

/**
 * Manages user operations
 */
public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public User createUser(String userId, String name, String email, String phone, String address) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User " + userId + " already exists");
        }
        
        User user = new User(userId, name, email, phone, address);
        users.put(userId, user);
        return user;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }

    public int getUserCount() {
        return users.size();
    }
}
