package dao;
import db.DatabaseManager;
import model.Album;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap; // Use TreeMap for sorted order of keys

public class AlbumDAO {

    public List<Album> getAllAlbums() throws SQLException {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT a.album_id, a.album_title, a.release_year, a.genre, a.is_in_collection, a.cover_art_path, ar.artist_name " +
                     "FROM albums a " +
                     "JOIN artists ar ON a.artist_id = ar.artist_id " +
                     "ORDER BY ar.artist_name, a.album_title";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Album album = new Album();
                album.setId(rs.getInt("album_id"));
                album.setTitle(rs.getString("album_title"));
                album.setReleaseYear(rs.getInt("release_year"));
                album.setGenre(rs.getString("genre"));
                album.setInCollection(rs.getBoolean("is_in_collection"));
                album.setArtistName(rs.getString("artist_name"));
                album.setCoverArtPath(rs.getString("cover_art_path"));
                albums.add(album);
            }
        }
        return albums;
    }
    public List<Album> searchAlbums(String query) throws SQLException {
        List<Album> albums = new ArrayList<>();
        String searchTerm = "%" + query + "%";
        String sql = "SELECT a.album_id, a.album_title, a.release_year, a.genre, a.is_in_collection, a.cover_art_path, ar.artist_name " +
                     "FROM albums a " +
                     "JOIN artists ar ON a.artist_id = ar.artist_id " +
                     "WHERE a.album_title LIKE ? OR ar.artist_name LIKE ? " +
                     "ORDER BY ar.artist_name, a.album_title";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Album album = new Album();
                    album.setId(rs.getInt("album_id"));
                    album.setTitle(rs.getString("album_title"));
                    album.setReleaseYear(rs.getInt("release_year"));
                    album.setGenre(rs.getString("genre"));
                    album.setInCollection(rs.getBoolean("is_in_collection"));
                    album.setArtistName(rs.getString("artist_name"));
                    album.setCoverArtPath(rs.getString("cover_art_path"));
                    albums.add(album);
                }
            }
        }
        return albums;
    }
    public void addAlbum(Album album) throws SQLException {
        String sql = "INSERT INTO albums (album_title, release_year, genre, is_in_collection, artist_id, cover_art_path) VALUES (?, ?, ?, ?, ?, ?)";
        int artistId = getOrCreateArtistId(album.getArtistName());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, album.getTitle());
            pstmt.setInt(2, album.getReleaseYear());
            pstmt.setString(3, album.getGenre());
            pstmt.setBoolean(4, album.isInCollection());
            pstmt.setInt(5, artistId);
            pstmt.setString(6, album.getCoverArtPath());
            pstmt.executeUpdate();
        }
    }
    public void updateAlbum(Album album) throws SQLException {
        String sql = "UPDATE albums SET album_title = ?, release_year = ?, genre = ?, is_in_collection = ?, artist_id = ?, cover_art_path = ? WHERE album_id = ?";
        int artistId = getOrCreateArtistId(album.getArtistName());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, album.getTitle());
            pstmt.setInt(2, album.getReleaseYear());
            pstmt.setString(3, album.getGenre());
            pstmt.setBoolean(4, album.isInCollection());
            pstmt.setInt(5, artistId);
            pstmt.setString(6, album.getCoverArtPath());
            pstmt.setInt(7, album.getId());
            pstmt.executeUpdate();
        }
    }
    public void deleteAlbum(int albumId) throws SQLException {
        String sql = "DELETE FROM albums WHERE album_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, albumId);
            pstmt.executeUpdate();
        }
    }
    private int getOrCreateArtistId(String artistName) throws SQLException {
        String checkSql = "SELECT artist_id FROM artists WHERE artist_name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, artistName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("artist_id");
                }
            }
        }
        String insertSql = "INSERT INTO artists(artist_name) VALUES(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, artistName);
            insertStmt.executeUpdate();
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating artist failed, no ID obtained.");
                }
            }
        }
    }
    // NEW METHOD: Get all artists
    public List<String> getAllArtists() throws SQLException {
        List<String> artists = new ArrayList<>();
        String sql = "SELECT artist_name FROM artists ORDER BY artist_name";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                artists.add(rs.getString("artist_name"));
            }
        }
        return artists;
    }
    // NEW METHOD: Update artist name
    public void updateArtistName(int artistId, String newName) throws SQLException {
        String sql = "UPDATE artists SET artist_name = ? WHERE artist_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, artistId);
            pstmt.executeUpdate();
        }
    }
    // NEW METHOD: Get artist ID by name
    public int getArtistId(String artistName) throws SQLException {
        String sql = "SELECT artist_id FROM artists WHERE artist_name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artistName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("artist_id");
                }
                return -1; // Indicate not found
            }
        }
    }
    // NEW METHOD: Delete Artist
    public void deleteArtist(int artistId) throws SQLException {
        String sql = "DELETE FROM artists WHERE artist_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, artistId);
            pstmt.executeUpdate();
        }
    }
    
    // NEW ANALYTICS METHOD: Total albums by genre
    public Map<String, Integer> countAlbumsByGenre() throws SQLException {
        Map<String, Integer> counts = new TreeMap<>();
        String sql = "SELECT genre, COUNT(*) as album_count FROM albums GROUP BY genre ORDER BY album_count DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                counts.put(rs.getString("genre"), rs.getInt("album_count"));
            }
        }
        return counts;
    }

    // NEW ANALYTICS METHOD: Albums added per year
    public Map<Integer, Integer> countAlbumsByYear() throws SQLException {
        Map<Integer, Integer> counts = new TreeMap<>();
        // Query to count albums, grouping by release_year, and ordering by year
        String sql = "SELECT release_year, COUNT(*) as album_count FROM albums GROUP BY release_year ORDER BY release_year";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                counts.put(rs.getInt("release_year"), rs.getInt("album_count"));
            }
        }
        return counts;
    }
}