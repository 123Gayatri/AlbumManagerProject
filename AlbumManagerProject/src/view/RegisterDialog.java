package view;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterDialog(Frame owner) {
        super(owner, "Register New User", true);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));

        Color primaryColor = new Color(52, 152, 219);
        Color secondaryColor = new Color(236, 240, 241);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(secondaryColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        // Style Text Fields
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        confirmPasswordField.setFont(fieldFont);

        usernameField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        userLabel.setFont(labelFont);
        passLabel.setFont(labelFont);
        confirmPassLabel.setFont(labelFont);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPassLabel);
        formPanel.add(confirmPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(secondaryColor);
        
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        registerButton.setFont(labelFont);
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        cancelButton.setFont(labelFont);
        cancelButton.setBackground(secondaryColor);
        cancelButton.setForeground(primaryColor);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
    }

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }
    public void addRegisterButtonListener(ActionListener listener) { registerButton.addActionListener(listener); }

    public void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
