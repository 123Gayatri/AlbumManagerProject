package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ArtistManagementDialog extends JDialog {
    private DefaultTableModel tableModel;
    private JTable artistTable;
    private JButton refreshButton, editButton, mergeButton, deleteButton;
    
    // Define Color/Font Constants from MainFrame theme
    private final Color PRIMARY_COLOR = new Color(44, 62, 80); // Dark Blue
    private final Color SECONDARY_COLOR = new Color(236, 240, 241); // Light Grey
    private final Color ACCENT_COLOR = new Color(52, 152, 219); // Bright Blue
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public ArtistManagementDialog(Frame owner, String dialogTitle) {
        super(owner, dialogTitle, true);
        setSize(550, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        
        getContentPane().setBackground(SECONDARY_COLOR);

        // --- Table Setup ---
        String[] columnNames = {"Artist Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        artistTable = new JTable(tableModel);
        artistTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        artistTable.setRowHeight(25);
        artistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artistTable.setSelectionBackground(ACCENT_COLOR);
        artistTable.setSelectionForeground(Color.WHITE);
        
        // Style Table Header
        artistTable.getTableHeader().setFont(TABLE_HEADER_FONT);
        artistTable.getTableHeader().setBackground(PRIMARY_COLOR);
        artistTable.getTableHeader().setForeground(Color.WHITE);
        artistTable.getTableHeader().setReorderingAllowed(false);
        artistTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Center-align the header text
        ((DefaultTableCellRenderer)artistTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(artistTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        
        // Add padding around the table
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(10, 10, 0, 10));
        tableWrapper.setBackground(SECONDARY_COLOR);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        add(tableWrapper, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(PRIMARY_COLOR); // Dark footer background
        buttonPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        refreshButton = new JButton("Refresh");
        editButton = new JButton("Edit Name (Stub)");
        mergeButton = new JButton("Merge Artists (Stub)");
        deleteButton = new JButton("Delete Artist (Stub)");

        // Apply MainFrame button styling
        styleButton(refreshButton, SECONDARY_COLOR, PRIMARY_COLOR);
        styleButton(editButton, ACCENT_COLOR, Color.WHITE);
        styleButton(mergeButton, new Color(230, 126, 34), Color.WHITE); // Orange for Merge
        styleButton(deleteButton, new Color(192, 57, 43), Color.WHITE); // Red for delete action

        buttonPanel.add(refreshButton);
        buttonPanel.add(editButton);
        buttonPanel.add(mergeButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial setup for buttons that are currently stubs
        editButton.setEnabled(false);
        mergeButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    // Helper method to style buttons, derived from MainFrame
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
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
    
    public void displayArtists(List<String> artists) {
        tableModel.setRowCount(0);
        for (String artist : artists) {
            Object[] row = { artist };
            tableModel.addRow(row);
        }
    }

    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    // Add stub listeners for completeness
    public void addEditListener(ActionListener listener) { editButton.addActionListener(listener); }
    public void addMergeListener(ActionListener listener) { mergeButton.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { deleteButton.addActionListener(listener); }
}
