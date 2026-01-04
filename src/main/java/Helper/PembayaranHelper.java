/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import java.util.UUID;
import java.security.SecureRandom;
import java.util.Random;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
/**
 *
 * @author LENOVO
 */
public class PembayaranHelper {
    public static String generateVA(String phone) {
    if (phone == null || phone.isEmpty()) {
        return "888" + (100000 + new java.util.Random().nextInt(900000));
    }
    return "888" + phone;
}
    public static String generateRandomQRContent() {
            String randomID = UUID.randomUUID().toString().substring(0, 8); 
            return "COZIFY-" + randomID.toUpperCase();
    }
    
    public static void generateQRCode(String text, int width, int height, String filePath) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // Bikin matrix bit buat QR Code
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Simpan jadi file gambar (PNG)
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    public static String generateEMoneyCode() {
        return "ID-" + (100000 + new Random().nextInt(900000));
    }
}
