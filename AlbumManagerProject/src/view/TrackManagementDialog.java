package view;

import model.Album;
import model.Track;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TrackManagementDialog extends JDialog {
    private DefaultTableModel tableModel;
    private JTable trackTable;
    private JButton refreshButton, addButton, editButton, deleteButton;
    private JLabel albumTitleLabel;
    private List<Track> currentTrackList = new ArrayList<>();
    private Album album;

    // Define Color/Font Constants from MainFrame theme
    private final Color PRIMARY_COLOR = new Color(44, 62, 80); // Dark Blue
    private final Color SECONDARY_COLOR = new Color(236, 240, 241); // Light Grey
    private final Color ACCENT_COLOR = new Color(52, 152, 219); // Bright Blue
    private final Color RED_COLOR = new Color(192, 57, 43); // Red
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public TrackManagementDialog(Frame owner, Album album) {
        super(owner, "Manage Tracks for Album", true);
        this.album = album;
        setSize(700, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(SECONDARY_COLOR);

        // --- Header Panel (Album Title) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        albumTitleLabel = new JLabel("Tracks for: " + album.getTitle());
        albumTitleLabel.setFont(HEADER_FONT);
        albumTitleLabel.setForeground(SECONDARY_COLOR);
        headerPanel.add(albumTitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {"#", "Track Title", "Favorite"}; // Added Favorite column
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        trackTable = new JTable(tableModel);
        trackTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        trackTable.setRowHeight(25);
        trackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trackTable.setSelectionBackground(ACCENT_COLOR);
        trackTable.setSelectionForeground(Color.WHITE);

        // Style Table Header
        trackTable.getTableHeader().setFont(TABLE_HEADER_FONT);
        trackTable.getTableHeader().setBackground(PRIMARY_COLOR);
        trackTable.getTableHeader().setForeground(Color.WHITE);
        trackTable.getTableHeader().setReorderingAllowed(false);
        trackTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scrollPane = new JScrollPane(trackTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(0, 10, 0, 10));
        tableWrapper.setBackground(SECONDARY_COLOR);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(PRIMARY_COLOR); // Dark footer background
        buttonPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        refreshButton = new JButton("Refresh");
        addButton = new JButton("Add Track");
        editButton = new JButton("Edit Track");
        deleteButton = new JButton("Delete Track");

        // Apply MainFrame button styling
        styleButton(refreshButton, SECONDARY_COLOR, PRIMARY_COLOR);
        styleButton(addButton, ACCENT_COLOR, Color.WHITE);
        styleButton(editButton, new Color(155, 89, 182), Color.WHITE); // Purple for Edit
        styleButton(deleteButton, RED_COLOR, Color.WHITE);

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setAlbumTitle(String title) {
        albumTitleLabel.setText("Tracks for: " + title);
    }

    public void displayTracks(List<Track> tracks) {
        this.currentTrackList = tracks;
        tableModel.setRowCount(0);
        for (Track track : tracks) {
            Object[] row = { track.getTrackNumber(), track.getTrackTitle(), track.isFavorite() ? "⭐" : "" };
            tableModel.addRow(row);
        }
    }

    public Track getSelectedTrack() {
        int selectedRow = trackTable.getSelectedRow();
        if (selectedRow >= 0) {
            return currentTrackList.get(selectedRow);
        }
        return null;
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    public void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Track Management", messageType);
    }

    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm Action", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Listener methods
    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addAddTrackListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addEditTrackListener(ActionListener listener) { editButton.addActionListener(listener); }
    public void addDeleteTrackListener(ActionListener listener) { deleteButton.addActionListener(listener); }
}