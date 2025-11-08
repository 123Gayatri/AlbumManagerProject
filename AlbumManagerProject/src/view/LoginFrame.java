package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.MatteBorder;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        setTitle("Album Manager - Login");
        // Increased size for a bolder, more filled look
        setSize(550, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Custom Styling ---
        // Increased font sizes
        Font labelFont = new Font("Segoe UI", Font.BOLD, 18); // Increased from 14
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18); // Increased from 14
        Font titleFont = new Font("Segoe UI", Font.BOLD, 40); // Increased from 32
        
        Color primaryColor = new Color(52, 152, 219); // A pleasing blue
        Color secondaryColor = new Color(236, 240, 241); // Light grey
        Color buttonHoverColor = new Color(41, 128, 185); // Darker blue

        // Main panel with a border layout and a modern background
        JPanel mainPanel = new JPanel(new BorderLayout(30, 30)); // Increased vertical spacing
        mainPanel.setBackground(secondaryColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Increased padding

        // Title Label
        JLabel titleLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(primaryColor);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel using GridBagLayout for flexible alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); // Increased internal padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(25); // Increased width
        usernameField.setFont(labelFont);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 3, 0, primaryColor), // Thicker bottom border
            BorderFactory.createEmptyBorder(8, 8, 8, 8) // Increased field padding
        ));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(25); // Increased width
        passwordField.setFont(labelFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 3, 0, primaryColor), // Thicker bottom border
            BorderFactory.createEmptyBorder(8, 8, 8, 8) // Increased field padding
        ));
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); // Increased button spacing
        buttonPanel.setOpaque(false);
        
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Style Login Button
        loginButton.setFont(buttonFont);
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(12, 30, 12, 30) // Increased button size
        ));

        // Style Register Button
        registerButton.setFont(buttonFont);
        registerButton.setBackground(secondaryColor);
        registerButton.setForeground(primaryColor);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(12, 30, 12, 30) // Increased button size
        ));

        // Add a simple hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(buttonHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(primaryColor);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public void addLoginButtonListener(ActionListener listener) { loginButton.addActionListener(listener); }
    public void addRegisterButtonListener(ActionListener listener) { registerButton.addActionListener(listener); }
    public void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
