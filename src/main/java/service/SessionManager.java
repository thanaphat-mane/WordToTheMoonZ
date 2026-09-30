package service;
import model.User;

public interface SessionManager {

void login(User user);

void logout();

User getCurrentUser();

boolean isLoggedIn();
}
