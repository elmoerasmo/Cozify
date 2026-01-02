package Controller;

import Model.User;

/**
 * Interface untuk controller yang membutuhkan data User
 * Digunakan untuk mengirim data user dari login ke dashboard
 */
public interface ControllerWithUser {
    /**
     * Method untuk set user yang sedang login
     * @param user User yang berhasil login
     */
    void setUser(User user);
}
