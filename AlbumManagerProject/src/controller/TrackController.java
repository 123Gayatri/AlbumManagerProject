package controller;

import dao.TrackDAO;
import model.Album;
import model.Track;
import view.TrackManagementDialog;
import view.TrackDialog;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class TrackController {
    private TrackManagementDialog view;
    private TrackDAO dao;
    private Album album;

    public TrackController(TrackManagementDialog view, TrackDAO dao, Album album) {
        this.view = view;
        this.dao = dao;
        this.album = album;
        
        this.view.setAlbumTitle(album.getTitle());

        // Add Listeners
        this.view.addRefreshListener(e -> refreshTrackTable());
        this.view.addAddTrackListener(e -> showAddTrackDialog());
        this.view.addEditTrackListener(e -> showEditTrackDialog());
        this.view.addDeleteTrackListener(e -> deleteTrack());

        refreshTrackTable(); // Initial Load
    }

    private void refreshTrackTable() {
        try {
            List<Track> tracks = dao.getTracksByAlbumId(album.getId());
            view.displayTracks(tracks);
        } catch (SQLException e) {
            view.showMessage("Error loading tracks: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddTrackDialog() {
        TrackDialog dialog = new TrackDialog(view, album.getTitle(), null);
        dialog.setVisible(true);
        
        if (dialog.wasSaved()) {
            try {
                Track newTrack = dialog.getTrack();
                newTrack.setAlbumId(album.getId()); // Set the Album ID before saving
                dao.addTrack(newTrack);
                refreshTrackTable();
            } catch (SQLException e) {
                view.showMessage("Error adding track: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditTrackDialog() {
        Track selectedTrack = view.getSelectedTrack();
        if (selectedTrack == null) {
            view.showMessage("Please select a track to edit.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        TrackDialog dialog = new TrackDialog(view, album.getTitle(), selectedTrack);
        dialog.setVisible(true);
        
        if (dialog.wasSaved()) {
            try {
                // The track object in the list is the same one being edited by the dialog.
                // We just need to update it in the database.
                dao.updateTrack(dialog.getTrack()); // NEW DAO METHOD REQUIRED
                refreshTrackTable();
            } catch (SQLException e) {
                view.showMessage("Error updating track: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteTrack() {
        Track selectedTrack = view.getSelectedTrack();
        if (selectedTrack == null) {
            view.showMessage("Please select a track to delete.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (view.showConfirmDialog("Are you sure you want to delete track " + selectedTrack.getTrackNumber() + " - '" + selectedTrack.getTrackTitle() + "'?")) {
            try {
                dao.deleteTrack(selectedTrack.getTrackId());
                refreshTrackTable();
            } catch (SQLException e) {
                view.showMessage("Error deleting track: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}