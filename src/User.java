public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isAdmin;

    public User(int id, String username, String email, String passwordHash, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    // Getters och setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isAdmin() { return isAdmin; }
}
