package model;

public class User {
    private int id;
    private String username;
    private String role;

    public User() {
        // Default constructor for DAO
    }
    
    public User(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    // New Setters for DAO population
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }
}