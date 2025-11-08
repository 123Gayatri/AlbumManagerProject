package view;
import model.Album;
import model.User;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class MainFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable albumTable;
    private JButton addButton, editButton, deleteButton;
    private List<Album> currentAlbumList = new ArrayList<>();
    private JLabel userInfoLabel;
    private JLabel coverArtLabel;
    private JLabel detailTitleLabel, detailArtistLabel, detailYearLabel, detailGenreLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;

    // NEW FIELD for Total Album Count
    private JLabel albumCountLabel;

    // NEW FIELD for View Tracks Button
    private JButton viewTracksButton;

    // RESTORED FIELD for Admin Button
    private JButton adminToolsButton;

    // NEW FIELD for Logout Button
    private JButton logoutButton;

    // Fields for the pop-up menu items (needed for listeners)
    private JMenuItem manageUsersItem;
    private JMenuItem manageArtistsItem;
    private JMenuItem showAnalyticsItem; 

    public MainFrame() {
        setTitle("Album Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- CUSTOM STYLES ---
        Color primaryColor = new Color(44, 62, 80); // Dark, elegant blue
        Color secondaryColor = new Color(236, 240, 241); // Light grey
        Color accentColor = new Color(52, 152, 219); // Bright blue for highlights
        Font headerFont = new Font("Segoe UI", Font.BOLD, 28);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font detailFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 22);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
        // --- HEADER PANEL (with Search Bar and Admin Button) ---
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("My Album Collection");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(secondaryColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // --- Search and Admin Button Panel (CENTER) ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);

        // Search elements
        searchField = new JTextField(20);
        searchField.setFont(tableFont);
        searchField.setBorder(BorderFactory.createLineBorder(accentColor, 1));
        searchButton = new JButton("Search");
        clearSearchButton = new JButton("Show All");
        styleSearchButton(searchButton, accentColor, Color.WHITE, buttonFont);
        styleSearchButton(clearSearchButton, secondaryColor, primaryColor, buttonFont);

        JLabel findLabel = new JLabel("Find:");
        findLabel.setForeground(secondaryColor);
        centerPanel.add(findLabel);
        centerPanel.add(searchField);
        centerPanel.add(searchButton);
        centerPanel.add(clearSearchButton);

        // RESTORED: Admin Tools Button setup
        adminToolsButton = new JButton("Admin Tools");
        styleSearchButton(adminToolsButton, new Color(230, 126, 34), Color.WHITE, buttonFont);
        centerPanel.add(adminToolsButton);

        // Setup Pop-up Menu for Admin Tools
        setupAdminPopupMenu();

        // RESTORED: Admin Tools button listener
        adminToolsButton.addActionListener(e -> {
            JPopupMenu menu = (JPopupMenu) adminToolsButton.getClientProperty("popup");
            if (menu != null) {
                menu.show(adminToolsButton, 0, adminToolsButton.getHeight());
            }
        });
        headerPanel.add(centerPanel, BorderLayout.CENTER);

        // --- User Info and Logout Panel (EAST) ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        userInfoLabel = new JLabel();
        userInfoLabel.setFont(labelFont);
        userInfoLabel.setForeground(secondaryColor);
        userPanel.add(userInfoLabel);

        // NEW: Logout Button setup
        logoutButton = new JButton("Logout");
        styleSearchButton(logoutButton, new Color(192, 57, 43), Color.WHITE, buttonFont); // Red color for exit
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);
        // --- END Header Panel Setup ---
        
        // --- ALBUM TABLE (Left Side) ---
        String[] columnNames = {"Artist", "Album Title", "Year", "In Collection"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        albumTable = new JTable(tableModel);
        albumTable.setFont(tableFont);
        albumTable.setRowHeight(35);
        albumTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        albumTable.getTableHeader().setBackground(primaryColor.darker());
        albumTable.getTableHeader().setForeground(Color.WHITE);
        albumTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        albumTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumTable.setSelectionBackground(accentColor);
        albumTable.setSelectionForeground(Color.WHITE);

        albumTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateDetailsPanel();
            }
        });

        JScrollPane scrollPane = new JScrollPane(albumTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        
        // --- Total Album Count Label (Below Table) ---
        albumCountLabel = new JLabel("Total Albums: 0", SwingConstants.CENTER); // Initial text
        albumCountLabel.setFont(labelFont.deriveFont(Font.BOLD, 14));
        albumCountLabel.setForeground(primaryColor);
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(secondaryColor);
        footerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        footerPanel.add(albumCountLabel, BorderLayout.CENTER);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // --- DETAILS PANEL (Right Side) ---
        JPanel detailsPanel = new JPanel(new BorderLayout(20, 20));
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(Color.WHITE);

        coverArtLabel = new JLabel();
        coverArtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverArtLabel.setPreferredSize(new Dimension(500, 500));
        coverArtLabel.setBorder(BorderFactory.createLineBorder(primaryColor, 2));
        detailsPanel.add(coverArtLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        detailTitleLabel = new JLabel("Title: ");
        detailArtistLabel = new JLabel("Artist: ");
        detailYearLabel = new JLabel("Year: ");
        detailGenreLabel = new JLabel("Genre: ");
        detailTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        detailArtistLabel.setFont(labelFont);
        detailYearLabel.setFont(detailFont);
        detailGenreLabel.setFont(detailFont);

        infoPanel.add(detailTitleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(detailArtistLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(detailYearLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(detailGenreLabel);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        
        // --- SPLIT PANE ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, detailsPanel);
        splitPane.setDividerLocation(0.4);
        splitPane.setDividerSize(5);
        
        // --- BUTTON TOOLBAR ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(primaryColor);
        
        addButton = new JButton("Add New Album");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");
        viewTracksButton = new JButton("View Tracks");
        
        addButton.setFont(buttonFont);
        editButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        viewTracksButton.setFont(buttonFont);
        
        styleButton(addButton, Color.WHITE);
        styleButton(editButton, Color.WHITE);
        styleButton(deleteButton, Color.WHITE);
        styleButton(viewTracksButton, accentColor);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewTracksButton);
        
        // --- MAIN LAYOUT ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 0));
        mainContentPanel.add(splitPane, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        updateDetailsPanel(); // Set initial state
    }

    // UPDATED METHOD: Setup the Admin Pop-up Menu with custom styling
    private void setupAdminPopupMenu() {
        JPopupMenu adminMenu = new JPopupMenu();

        // Define menu item font
        Font menuItemFont = new Font("Segoe UI", Font.BOLD, 14);
        manageUsersItem = new JMenuItem("Manage Users");
        manageArtistsItem = new JMenuItem("Manage Artists");
        showAnalyticsItem = new JMenuItem("Show Analytics Dashboard");

        // Apply custom font to the menu items
        manageUsersItem.setFont(menuItemFont);
        manageArtistsItem.setFont(menuItemFont);
        showAnalyticsItem.setFont(menuItemFont);
        
        adminMenu.add(manageUsersItem);
        adminMenu.add(manageArtistsItem);
        adminMenu.add(new JSeparator());
        adminMenu.add(showAnalyticsItem);

        // RESTORED: Storing the menu on the button's client property
        adminToolsButton.putClientProperty("popup", adminMenu);
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        // Add a mouse listener for hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220)); // Light grey on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    // Helper method for styling header buttons (search/admin/logout)
    private void styleSearchButton(JButton button, Color backgroundColor, Color foregroundColor, Font font) {
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
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
    private void updateDetailsPanel() {
        Album selectedAlbum = getSelectedAlbum();
        if (selectedAlbum != null) {
            detailTitleLabel.setText(selectedAlbum.getTitle());
            detailArtistLabel.setText("by " + selectedAlbum.getArtistName());
            detailYearLabel.setText("Released in " + selectedAlbum.getReleaseYear());
            detailGenreLabel.setText("Genre: " + selectedAlbum.getGenre());
            
            // Show tracks button only when an album is selected
            viewTracksButton.setEnabled(true);

            String path = selectedAlbum.getCoverArtPath();
            if (path != null && !path.isEmpty()) {
                try {
                    BufferedImage img = ImageIO.read(new File(path));
                    int labelWidth = coverArtLabel.getPreferredSize().width;
                    int labelHeight = coverArtLabel.getPreferredSize().height;
                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();

                    // Calculate scaling to fit within the label
                    double scaleX = (double) labelWidth / imageWidth;
                    double scaleY = (double) labelHeight / imageHeight;
                    double scale = Math.min(scaleX, scaleY);
                    int newWidth = (int) (scale * imageWidth);
                    int newHeight = (int) (scale * imageHeight);
                    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    coverArtLabel.setIcon(new ImageIcon(scaledImg));
                    coverArtLabel.setText("");
                } catch (IOException e) {
                    coverArtLabel.setIcon(null);
                    coverArtLabel.setText("Image not found");
                }
            } else {
                coverArtLabel.setIcon(null);
                coverArtLabel.setText("No Cover Art");
            }
        } else {
            detailTitleLabel.setText("No Album Selected");
            detailArtistLabel.setText("");
            detailYearLabel.setText("");
            detailGenreLabel.setText("");
            coverArtLabel.setIcon(null);
            coverArtLabel.setText("Select an album to see details");
            viewTracksButton.setEnabled(false); // Disable tracks button when no album is selected
        }
    }

    public void displayAlbums(List<Album> albums) {
        this.currentAlbumList = albums;
        tableModel.setRowCount(0);
        for (Album album : albums) {
            // Updated columns: Artist, Album Title, Year, In Collection
            Object[] row = { album.getArtistName(), album.getTitle(), album.getReleaseYear(), album.isInCollection() ? "Yes" : "No" }; 
            tableModel.addRow(row);
        }
    }

    public void setUserInfo(User user) {
        userInfoLabel.setText("Welcome, " + user.getUsername() + " (Role: " + user.getRole() + ")");
    }
    
    // NEW METHOD to set album count
    public void setAlbumCount(String text) {
        albumCountLabel.setText(text);
    }

    public Album getSelectedAlbum() {
        int selectedRow = albumTable.getSelectedRow();
        if (selectedRow >= 0) {
            return currentAlbumList.get(selectedRow);
        }
        return null;
    }

    // setAdminControlsVisible only affects CRUD buttons
    public void setAdminControlsVisible(boolean visible) {
        editButton.setVisible(visible);
        deleteButton.setVisible(visible);
    }

    public void setAddButtonVisible(boolean visible) {
        addButton.setVisible(visible);
    }
    
    // NEW METHOD to control visibility of View Tracks button
    public void setTrackManagementVisible(boolean visible) {
        viewTracksButton.setVisible(visible);
    }
    
    // NEW METHOD to control visibility of Analytics menu item
    public void setAnalyticsVisible(boolean visible) {
        showAnalyticsItem.setVisible(visible);
        // Controls the visibility of the main button if it is the only menu item.
        adminToolsButton.setVisible(manageUsersItem.isVisible() || manageArtistsItem.isVisible() || showAnalyticsItem.isVisible());
    }

    // Control visibility of the Admin Tools button
    public void setManageUsersVisible(boolean visible) {
        manageUsersItem.setVisible(visible);
        // Show the main button if any management tool or analytics is visible
        adminToolsButton.setVisible(manageUsersItem.isVisible() || manageArtistsItem.isVisible() || showAnalyticsItem.isVisible());
    }
    // Control visibility of the Admin Tools button
    public void setManageArtistsVisible(boolean visible) {
        manageArtistsItem.setVisible(visible);
        // Show the main button if any management tool or analytics is visible
        adminToolsButton.setVisible(manageUsersItem.isVisible() || manageArtistsItem.isVisible() || showAnalyticsItem.isVisible());
    }

    public String getSearchQuery() {
        return searchField.getText();
    }

    // LISTENER METHODS now hook up to the JMenuItems
    public void addManageUsersListener(ActionListener listener) {
        manageUsersItem.addActionListener(listener);
    }
    public void addManageArtistsListener(ActionListener listener) {
        manageArtistsItem.addActionListener(listener);
    }
    
    // NEW LISTENER METHOD for Show Analytics
    public void addShowAnalyticsListener(ActionListener listener) {
        showAnalyticsItem.addActionListener(listener);
    }
    
    // NEW LISTENER METHOD for View Tracks button
    public void addViewTracksListener(ActionListener listener) {
        viewTracksButton.addActionListener(listener);
    }


    // NEW LISTENER METHOD for Logout button
    public void addLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    public void addSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    public void addClearSearchButtonListener(ActionListener listener) {
        clearSearchButton.addActionListener(listener);
    }
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public void addAddButtonListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addEditButtonListener(ActionListener listener) { editButton.addActionListener(listener); }
    public void addDeleteButtonListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    
    // This is for error messages with title/type (used in controllers)
    public void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}