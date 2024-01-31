package com.bus_season_ticket.capstone_project.QRcode;


import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class QRCodeService {

    @Autowired
    private QRCodeRepository qrCodeRepository;
    @Autowired
    private final UserRepo userRepo;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final String FORMAT = "PNG";

    public QRCodeService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean saveQRCode(UUID userId) throws WriterException, IOException {
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){
        User updateUser = user.get();

        String email = user.get().getEmail();

        try{
            if(user.get().getId().equals(userId)){
                String base64QRCode = generateQRCode(email, userId);

                QRCode qrCode = new QRCode();
                qrCode.setEmail(email);
                qrCode.setUserId(userId);
                qrCode.setQRCodeData(base64QRCode);

                updateUser.setQrCode(qrCode);
                qrCodeRepository.save(qrCode);
                userRepo.save(updateUser);
                return true;
            }
        }catch (Exception e){
            return false;
        }
        }
            return false;
    }
        public static String generateQRCode(String email, UUID userId) throws WriterException, IOException {

            String qrCodeData = "email: " + email + "\n" + "userId: " + userId;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrCodeData, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);

            BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Add text on the image
            Graphics2D graphics = qrCodeImage.createGraphics();
            Font font = new Font("Arial", Font.PLAIN, 12);
            graphics.setFont(font);
            graphics.setColor(Color.BLACK);
            graphics.drawString("Bus season Ticket. "+"User email:"+ email, 10, HEIGHT - 10);

            // Convert the image to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, FORMAT, outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            // Convert byte array to base64 for storage in the database
            return Base64.getEncoder().encodeToString(qrCodeBytes);
        }

    public Optional<QRCode> getQRCodeById(Integer id) {

        return qrCodeRepository.findById(id);
    }

    public ResponseEntity<ByteArrayResource> downloadQRCodeImageById(String userId) {
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        if(user.isPresent()) {
            Integer id = user.get().getQrCode().getId();
            Optional<QRCode> qrCodeOptional = qrCodeRepository.findById(id);

            if (qrCodeOptional.isPresent()) {
                QRCode qrCode = qrCodeOptional.get();
                String base64QRCode = qrCode.getQRCodeData();

                if (!StringUtils.isEmpty(base64QRCode)) {
                    byte[] imageBytes = Base64.getDecoder().decode(base64QRCode);

                    ByteArrayResource resource = new ByteArrayResource(imageBytes);

                    // Set headers to prompt download
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qr_code.png");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentLength(imageBytes.length)
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(resource);
                }
            }
        }
        // If QR code or data not found, return a ResponseEntity with an appropriate status
        return ResponseEntity.notFound().build();
    }

}
