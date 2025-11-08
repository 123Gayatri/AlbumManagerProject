package dao;
import db.DatabaseManager;
import model.Track;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Data Access Object for managing Track records in the database.
 */
public class TrackDAO {
    /**
     * Retrieves all tracks associated with a given album ID, ordered by track number.
     */
    public List<Track> getTracksByAlbumId(int albumId) throws SQLException {
        List<Track> tracks = new ArrayList<>();
        // Include is_favorite for the new column in the tracks table
        String sql = "SELECT track_id, track_number, track_title, album_id, is_favorite FROM tracks WHERE album_id = ? ORDER BY track_number";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Track track = new Track();
                    track.setTrackId(rs.getInt("track_id"));
                    track.setAlbumId(rs.getInt("album_id"));
                    track.setTrackNumber(rs.getInt("track_number"));
                    track.setTrackTitle(rs.getString("track_title"));
                    track.setFavorite(rs.getBoolean("is_favorite")); // Set the new field
                    tracks.add(track);
                }
            }
        }
        return tracks;
    }
    /**
     * Adds a new track to an album.
     */
    public void addTrack(Track track) throws SQLException {
        // Include is_favorite in the insert statement
        String sql = "INSERT INTO tracks (album_id, track_number, track_title, is_favorite) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, track.getAlbumId());
            pstmt.setInt(2, track.getTrackNumber());
            pstmt.setString(3, track.getTrackTitle());
            pstmt.setBoolean(4, track.isFavorite()); // Set is_favorite
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Updates an existing track's number, title, and favorite status. (NEW METHOD)
     */
    public void updateTrack(Track track) throws SQLException {
        String sql = "UPDATE tracks SET track_number = ?, track_title = ?, is_favorite = ? WHERE track_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, track.getTrackNumber());
            pstmt.setString(2, track.getTrackTitle());
            pstmt.setBoolean(3, track.isFavorite());
            pstmt.setInt(4, track.getTrackId());
            
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a track by its ID.
     */
    public void deleteTrack(int trackId) throws SQLException {
        String sql = "DELETE FROM tracks WHERE track_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trackId);
            pstmt.executeUpdate();
        }
    }
}