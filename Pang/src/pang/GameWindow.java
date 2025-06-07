package pang;

import javax.swing.*;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;

    public GameWindow() {
        setTitle("Pang Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);

        createMenuBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Pang Game Menüsü
        JMenu gameMenu = new JMenu("Game");
        JMenuItem registerItem = new JMenuItem("Register");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem newGameItem = new JMenuItem("New");
        JMenuItem quitItem = new JMenuItem("Quit");

        registerItem.addActionListener(e -> LoginDialog.showRegister(this));
        loginItem.addActionListener(e -> LoginDialog.showLogin(this));
        newGameItem.addActionListener(e -> startGame());
        quitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(registerItem);
        gameMenu.add(loginItem);
        gameMenu.addSeparator();
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(quitItem);

        // Options Menüsü
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem highScoreItem = new JMenuItem("High Score");
        JMenuItem historyItem = new JMenuItem("History");

        optionsMenu.add(highScoreItem);
        optionsMenu.add(historyItem);

        // Help Menüsü
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void startGame() {
        if (!UserManager.isLoggedIn()) {
            LoginDialog.showLogin(this);
        } else {
            gamePanel.startGame(); // Oyunu başlatıyor
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Developer: Deniz ŞAHİN\nSchool No: 20210702062\nEmail: deniz.sahin3@std.yeditepe.edu.tr",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}