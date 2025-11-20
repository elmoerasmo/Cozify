/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.UUID;

/**
 *
 * @author LENOVO
 */
public class User {
    
    private UUID idUser ;
    private String username;
    private String password;

    public User(String username, String password) {
        this.setPass(password);
        this.setUname(username);
        UUID uuid = UUID.randomUUID();
        this.setUid(idUser);
    }
    
    public User(UUID idUser, String username, String password) {
        this.setPass(password);
        this.setUname(username);
        this.setUid(idUser);
    }

    
    public UUID getUid() {
        return idUser;
    }

    public void setUid(UUID idUser) {
        this.idUser = idUser;
    }

    public String getUname() {
        return username;
    }

    public void setUname(String username) {
        this.username = username;
    }

    public String getPass() {
        return password;
    }

    public void setPass(String password) {
        this.password = password;
    }
     
    
}
