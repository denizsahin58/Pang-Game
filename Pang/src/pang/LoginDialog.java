	package pang;

import javax.swing.*;
import java.awt.*;


public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isLoginMode;

    private LoginDialog(JFrame parent, boolean isLoginMode) {
        super(parent, isLoginMode ? "Login" : "Register", true);
        this.isLoginMode = isLoginMode;

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton actionButton = new JButton(isLoginMode ? "Login" : "Register");
        JButton cancelButton = new JButton("Cancel");

        panel.add(actionButton);
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);

        actionButton.addActionListener(e -> handleAction());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isLoginMode) {
            if (UserManager.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (UserManager.register(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Açık şekilde çağırılabilmesi için static metotlar:
    public static void showLogin(JFrame parent) {
        new LoginDialog(parent, true).setVisible(true);
    }

    public static void showRegister(JFrame parent) {
        new LoginDialog(parent, false).setVisible(true);
    }
}
