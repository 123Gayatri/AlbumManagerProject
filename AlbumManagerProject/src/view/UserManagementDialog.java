package view;

import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDialog extends JDialog {
    private DefaultTableModel tableModel;
    private JTable userTable;
    private JButton refreshButton, changeRoleButton, deleteButton;
    private List<User> currentUserList = new ArrayList<>();
    
    private static final String[] ROLES = {"USER", "TRACKER", "ADMIN"};
    
    // Define Color/Font Constants from MainFrame theme
    private final Color PRIMARY_COLOR = new Color(44, 62, 80); // Dark Blue
    private final Color SECONDARY_COLOR = new Color(236, 240, 241); // Light Grey
    private final Color ACCENT_COLOR = new Color(52, 152, 219); // Bright Blue
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public UserManagementDialog(Frame owner, String dialogTitle) {
        super(owner, dialogTitle, true);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        
        getContentPane().setBackground(SECONDARY_COLOR); // Set dialog background

        // --- Table Setup ---
        String[] columnNames = {"ID", "Username", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.setRowHeight(25);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setSelectionBackground(ACCENT_COLOR);
        userTable.setSelectionForeground(Color.WHITE);
        
        // Style Table Header
        userTable.getTableHeader().setFont(TABLE_HEADER_FONT);
        userTable.getTableHeader().setBackground(PRIMARY_COLOR);
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Center-align the header text
        ((DefaultTableCellRenderer)userTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        
        // Add padding around the table
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(10, 10, 0, 10));
        tableWrapper.setBackground(SECONDARY_COLOR);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        add(tableWrapper, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(PRIMARY_COLOR); // Dark footer background
        buttonPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        refreshButton = new JButton("Refresh");
        changeRoleButton = new JButton("Change Role");
        deleteButton = new JButton("Delete User");

        // Apply MainFrame button styling
        styleButton(refreshButton, SECONDARY_COLOR, PRIMARY_COLOR);
        styleButton(changeRoleButton, ACCENT_COLOR, Color.WHITE);
        styleButton(deleteButton, new Color(192, 57, 43), Color.WHITE); // Red for delete action

        buttonPanel.add(refreshButton);
        buttonPanel.add(changeRoleButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Helper method to style buttons, derived from MainFrame
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        // Add a mouse listener for hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }
    
    public void displayUsers(List<User> users) {
        this.currentUserList = users;
        tableModel.setRowCount(0);
        for (User user : users) {
            Object[] row = { user.getId(), user.getUsername(), user.getRole() };
            tableModel.addRow(row);
        }
    }
    
    public User getSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            return currentUserList.get(selectedRow);
        }
        return null;
    }
    
    public String promptForNewRole(String currentRole) {
        String newRole = (String) JOptionPane.showInputDialog(
            this,
            "Select new role for the user:",
            "Change Role",
            JOptionPane.QUESTION_MESSAGE,
            null,
            ROLES,
            currentRole
        );
        return newRole;
    }
    
    public void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "User Management", messageType);
    }
    
    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addChangeRoleListener(ActionListener listener) { changeRoleButton.addActionListener(listener); }
    public void addDeleteUserListener(ActionListener listener) { deleteButton.addActionListener(listener); }
}
