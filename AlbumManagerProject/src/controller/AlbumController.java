package controller;
import dao.AlbumDAO;
import dao.TrackDAO;
import dao.UserDAO;
import model.Album;
import model.User;
import view.AlbumDialog;
import view.LoginFrame;
import view.MainFrame;
import view.UserManagementDialog;
import view.ArtistManagementDialog;
import view.TrackManagementDialog;
import view.AnalyticsDialog;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AlbumController {
    private MainFrame view;
    private AlbumDAO dao;
    private User user;
    private UserDAO userDAO;
    private TrackDAO trackDAO;

    public AlbumController(MainFrame view, AlbumDAO dao, User user) {
        this.view = view;
        this.dao = dao;
        this.user = user;
        this.userDAO = new UserDAO();
        this.trackDAO = new TrackDAO();

        this.view.setUserInfo(user);
        // Add listeners for CRUD buttons
        this.view.addAddButtonListener(e -> showAddDialog());
        this.view.addEditButtonListener(e -> showEditDialog());
        this.view.addDeleteButtonListener(e -> deleteAlbum());
        
        // Add listeners for Tracks and Analytics
        this.view.addViewTracksListener(e -> showTrackManagementDialog());
        this.view.addShowAnalyticsListener(e -> showAnalyticsDashboard());

        // Add listeners for Search Buttons
        this.view.addSearchButtonListener(e -> performSearch());
        this.view.addClearSearchButtonListener(e -> refreshAlbumTable());

        // Listeners for Admin Tools (attached to JMenuItems inside the pop-up)
        this.view.addManageUsersListener(e -> showUserManagementDialog());
        this.view.addManageArtistsListener(e -> showArtistManagementDialog());

        // Logout Listener
        this.view.addLogoutButtonListener(e -> performLogout());
        setupRoleBasedAccess();
        refreshAlbumTable();
    }

    private void performSearch() {
        String query = view.getSearchQuery();
        if (query == null || query.trim().isEmpty()) {
            view.showMessage("Please enter a search term.");
            return;
        }
        try {
            List<Album> searchResults = dao.searchAlbums(query);
            view.displayAlbums(searchResults);
            if (searchResults.isEmpty()) {
                view.showMessage("No albums found matching '" + query + "'.");
            }
            updateAlbumCount(searchResults.size());
        } catch (SQLException e) {
            view.showMessage("Error performing search: " + e.getMessage());
        }
    }

    private void refreshAlbumTable() {
        try {
            List<Album> allAlbums = dao.getAllAlbums();
            view.displayAlbums(allAlbums);
            updateAlbumCount(allAlbums.size());
        } catch (SQLException e) {
            view.showMessage("Error loading albums: " + e.getMessage());
        }
    }
    
    private void updateAlbumCount(int count) {
        view.setAlbumCount("Total Albums: " + count);
    }


    private void showAddDialog() {
        AlbumDialog dialog = new AlbumDialog(view, "Add New Album", null);
        dialog.setVisible(true);
        if (dialog.wasSaved()) {
            try {
                dao.addAlbum(dialog.getAlbum());
                refreshAlbumTable();
            } catch (SQLException e) {
                view.showMessage("Error adding album: " + e.getMessage());
            }
        }
    }

    private void showEditDialog() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.showMessage("Please select an album to edit.");
            return;
        }
        AlbumDialog dialog = new AlbumDialog(view, "Edit Album", selectedAlbum);
        dialog.setVisible(true);
        if (dialog.wasSaved()) {
            try {
                dao.updateAlbum(dialog.getAlbum());
                refreshAlbumTable();
            } catch (SQLException e) {
                view.showMessage("Error updating album: " + e.getMessage());
            }
        }
    }

    private void deleteAlbum() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.showMessage("Please select an album to delete.");
            return;
        }
        if (view.showConfirmDialog("Are you sure you want to delete '" + selectedAlbum.getTitle() + "'?")) {
            try {
                dao.deleteAlbum(selectedAlbum.getId());
                refreshAlbumTable();
            } catch (SQLException e) {
                view.showMessage("Error deleting album: " + e.getMessage());
            }
        }
    }

    private void setupRoleBasedAccess() {
        String role = user.getRole().toUpperCase();

        // CRUD buttons: Only ADMIN can edit/delete
        view.setAdminControlsVisible("ADMIN".equals(role));
        
        // Menu Items:
        view.setManageUsersVisible("ADMIN".equals(role));
        view.setManageArtistsVisible("ADMIN".equals(role) || "TRACKER".equals(role));
        
        // Analytics is hidden from USER
        view.setAnalyticsVisible("ADMIN".equals(role) || "TRACKER".equals(role)); 
        
        // Add button visibility
        if ("ADMIN".equals(role) || "TRACKER".equals(role) || "USER".equals(role)) {
            view.setAddButtonVisible(true);
        } else {
            view.setAddButtonVisible(false);
        }
        
        // Track Management button visibility
        view.setTrackManagementVisible("ADMIN".equals(role) || "TRACKER".equals(role));
    }

    private void performLogout() {
        if (view.showConfirmDialog("Are you sure you want to log out?")) {
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginView = new LoginFrame();
                UserDAO userDAO = new UserDAO();
                new LoginController(loginView, userDAO);
                loginView.setVisible(true);
            });
        }
    }

    private void showUserManagementDialog() {
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            view.showMessage("Access Denied", "Only ADMIN can access User Management.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserManagementDialog userView = new UserManagementDialog(view, "Manage Users");
        new UserController(userView, userDAO);
        userView.setVisible(true);
    }

    private void showArtistManagementDialog() {
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"TRACKER".equalsIgnoreCase(user.getRole())) {
            view.showMessage("Access Denied", "Only ADMIN or TRACKER roles can manage artists.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArtistManagementDialog artistView = new ArtistManagementDialog(view, "Artist Management");

        // Anonymous Controller Logic: Refresh function
        artistView.addRefreshListener(e -> {
            try {
                artistView.displayArtists(dao.getAllArtists());
                view.showMessage("Artist list refreshed successfully.");
            } catch (SQLException ex) {
                view.showMessage("Error refreshing artists: " + ex.getMessage());
            }
        });
        // Anonymous Controller Logic: Stub listeners
        artistView.addEditListener(e -> view.showMessage("Edit Artist feature is a stub."));
        artistView.addMergeListener(e -> view.showMessage("Merge Artists feature is a stub."));
        artistView.addDeleteListener(e -> view.showMessage("Delete Artist feature is a stub."));
        // Initial load
        try {
            artistView.displayArtists(dao.getAllArtists());
        } catch (SQLException e) {
            view.showMessage("Error loading artists: " + e.getMessage());
        }
        artistView.setVisible(true);
    }
    
    // NEW METHOD: Show Track Management Dialog
    private void showTrackManagementDialog() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.showMessage("Please select an album to manage tracks.");
            return;
        }
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"TRACKER".equalsIgnoreCase(user.getRole())) {
            view.showMessage("Access Denied", "Only ADMIN or TRACKER roles can manage tracks.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        TrackManagementDialog trackView = new TrackManagementDialog(view, selectedAlbum);
        new TrackController(trackView, trackDAO, selectedAlbum);
        trackView.setVisible(true);
    }
    
    // NEW METHOD: Show Analytics Dashboard
    private void showAnalyticsDashboard() {
        try {
            Map<String, Integer> albumsByGenre = dao.countAlbumsByGenre();
            Map<Integer, Integer> albumsByYear = dao.countAlbumsByYear();
            
            AnalyticsDialog analyticsDialog = new AnalyticsDialog(view, albumsByGenre, albumsByYear);
            analyticsDialog.setVisible(true);
            
        } catch (SQLException e) {
            view.showMessage("Error loading analytics data: " + e.getMessage());
        }
    }
}