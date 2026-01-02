/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author LENOVO
 */
public class Session {
    private static User currentUser; // ini yang menyimpan session

    // setter untuk login
    public static void setUser(User user) {
        currentUser = user;
    }

    // getter untuk ambil data user di controller lain
    public static User getUser() {
        return currentUser;
    }

    // hapus session saat logout
    public static void clear() {
        currentUser = null;
    }

}
