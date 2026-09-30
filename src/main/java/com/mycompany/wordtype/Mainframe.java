package com.mycompany.wordtype;

import javax.swing.*;
import java.awt.*;

import service.*;
import model.*;

public class Mainframe extends JFrame {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_HOME = "home";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final SessionManager session = new InMemorySession();

    private final login loginScreen = new login();
    private final Home homeScreen = new Home();

    public Mainframe() {
        setTitle("WordType");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        cards.add(loginScreen.getRootPanel(), CARD_LOGIN);
        cards.add(homeScreen.getRootPanel(), CARD_HOME);
        setContentPane(cards);

        loginScreen.setOnLoginSuccess(user -> {
            session.login(user);
            showHome();
        });

        homeScreen.setOnUserButtonClick(() -> {
            if (session.isLoggedIn())
                session.logout();
            showLogin();
        });

        pack();                      // ขนาดหน้าต่างมาจาก preferredSize ของ panel (1200x700)
        setLocationRelativeTo(null); // วางกลางจอ
        showLogin();                 // เริ่มที่หน้า login
    }

    public void showLogin() {
        homeScreen.setUsername(null);
        cardLayout.show(cards, CARD_LOGIN);
        loginScreen.reset();
    }

    public void showHome() {
        User user = session.getCurrentUser();
        homeScreen.setUsername(user == null ? null : user.getUsername());
        cardLayout.show(cards, CARD_HOME);
    }
}