package Model;

import java.util.UUID;

public class User {
    private UUID idUser;
    private String username;
    private String email;
    private String password;

    // Constructor otomatis generate UUID
    public User(String username, String email, String password) {
        this.idUser = UUID.randomUUID(); // generate UUID baru
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter dan Setter
    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
