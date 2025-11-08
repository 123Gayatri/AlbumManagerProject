package model;

public class Album {
    private int id;
    private String title;
    private int releaseYear;
    private String genre;
    private String artistName;
    private boolean isInCollection;
    private String coverArtPath; // <-- ADD THIS LINE

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public boolean isInCollection() { return isInCollection; }
    public void setInCollection(boolean inCollection) { this.isInCollection = inCollection; }
    
    // v-- ADD THESE GETTER AND SETTER METHODS --v
    public String getCoverArtPath() { return coverArtPath; }
    public void setCoverArtPath(String coverArtPath) { this.coverArtPath = coverArtPath; }
}