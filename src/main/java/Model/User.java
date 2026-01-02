package Model;

public class User {
    protected int id;
    protected String nama;
    protected String email;
    protected String role;
    protected String status;
    protected String password;
    protected String noTelepon;


    public User() {}

    public User(int id, String nama, String email, String role, String status, String noTelepon) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.status = status;
        this.noTelepon = noTelepon;
        
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
     public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
}
