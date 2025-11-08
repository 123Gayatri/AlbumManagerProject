package controller;
import dao.AlbumDAO;
import dao.UserDAO;
import model.User;
import view.LoginFrame;
import view.MainFrame;
import view.RegisterDialog;
import javax.swing.*;
import java.sql.SQLException;
public class LoginController {
    private LoginFrame loginView;
    private UserDAO userDAO;
    public LoginController(LoginFrame loginView, UserDAO userDAO) {
        this.loginView = loginView;
        this.userDAO = userDAO;
        this.loginView.addLoginButtonListener(e -> attemptLogin());
        this.loginView.addRegisterButtonListener(e -> showRegisterDialog());
    }
    private void attemptLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Input Error", "Username and password cannot be empty.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            User user = userDAO.validateUser(username, password);
            if (user != null) {
                loginView.dispose();
                launchMainApplication(user);
            } else {
                loginView.showMessage("Login Failed", "Invalid username or password.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            loginView.showMessage("Database Error", "Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    private void launchMainApplication(User user) {
        MainFrame view = new MainFrame();
        AlbumDAO dao = new AlbumDAO();
        new AlbumController(view, dao, user);
        view.setVisible(true);
    }

    // NEW METHOD to handle the registration workflow
    private void showRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(loginView);
        dialog.addRegisterButtonListener(e -> attemptRegistration(dialog));
        dialog.setVisible(true);
    }
    // NEW METHOD to handle the registration process
    private void attemptRegistration(RegisterDialog dialog) {
        String username = dialog.getUsername();
        String password = dialog.getPassword();
        String confirmPassword = dialog.getConfirmPassword();
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            dialog.showMessage("Input Error", "All fields must be filled out.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            dialog.showMessage("Password Mismatch", "Passwords do not match.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            userDAO.addUser(username, password);
            dialog.showMessage("Success", "User registered successfully! You can now log in.", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        } catch (SQLException ex) {
            dialog.showMessage("Registration Error", "Error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
}