package controller;
import dao.UserDAO;
import model.User;
import view.UserManagementDialog;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
public class UserController {
    private UserManagementDialog view;
    private UserDAO dao;
    public UserController(UserManagementDialog view, UserDAO dao) {
        this.view = view;
        this.dao = dao;
        this.view.addRefreshListener(e -> refreshUserTable());
        this.view.addChangeRoleListener(e -> changeUserRole());
        this.view.addDeleteUserListener(e -> deleteUser());

        refreshUserTable(); // Initial load
    }
    private void refreshUserTable() {
        try {
            List<User> users = dao.getAllUsers();
            view.displayUsers(users);
        } catch (SQLException e) {
            view.showMessage("Error loading users: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    private void changeUserRole() {
        User selectedUser = view.getSelectedUser();
        if (selectedUser == null) {
            view.showMessage("Please select a user to change role.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String currentRole = selectedUser.getRole();
        String newRole = view.promptForNewRole(currentRole);
        if (newRole != null && !newRole.equalsIgnoreCase(currentRole)) {
            try {
                dao.updateUserRole(selectedUser.getId(), newRole);
                view.showMessage("Role updated successfully to " + newRole + " for " + selectedUser.getUsername() + ".", JOptionPane.INFORMATION_MESSAGE);
                refreshUserTable();
            } catch (SQLException e) {
                view.showMessage("Error updating role: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                view.showMessage(e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteUser() {
        User selectedUser = view.getSelectedUser();
        if (selectedUser == null) {
            view.showMessage("Please select a user to delete.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (view.showConfirmDialog("Are you sure you want to delete user '" + selectedUser.getUsername() + "'?")) {
            try {
                dao.deleteUser(selectedUser.getId());
                refreshUserTable();
                view.showMessage("User " + selectedUser.getUsername() + " deleted successfully.", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                view.showMessage("Error deleting user: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}