package model;
/**
 * Model class for a single track on an album.
 */
public class Track {
    private int trackId;
    private int albumId;
    private int trackNumber;
    private String trackTitle;
    private boolean isFavorite; // NEW FIELD
    public Track() {}
    // Getters and Setters
    public int getTrackId() { return trackId; }
    public void setTrackId(int trackId) { this.trackId = trackId; }
    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }
    public int getTrackNumber() { return trackNumber; }
    public void setTrackNumber(int trackNumber) { this.trackNumber = trackNumber; }
    public String getTrackTitle() { return trackTitle; }
    public void setTrackTitle(String trackTitle) { this.trackTitle = trackTitle; }
    
    // NEW GETTER AND SETTER
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}