package service;

import model.User;

public class InMemorySession implements SessionManager{

    private User currentuUser;

    @Override
    public void login(User user) {
        if(user == null) throw new IllegalArgumentException();
        this.currentuUser = user;
    }

    @Override
    public void logout() {
        this.currentuUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentuUser;
    }

    @Override
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
    
}
