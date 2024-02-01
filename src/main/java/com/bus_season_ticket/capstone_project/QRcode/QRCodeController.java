package com.bus_season_ticket.capstone_project.QRcode;




import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/qrcodes")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getQRCodeById(@PathVariable String userId) {
        // Fetch QR code data from the database based on the provided ID

        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        int id = user.get().getQrCode().getId();
        Optional<QRCode> qrCodeEntity = qrCodeService.getQRCodeById(id);

        if (qrCodeEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Decode the base64 string back to byte array
        byte[] decodedBytes = Base64.getDecoder().decode(qrCodeEntity.get().getQRCodeData());

        // Set appropriate headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(decodedBytes.length);

        return new ResponseEntity<>(decodedBytes, headers, HttpStatus.OK);
    }



    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadQRCodeById(@PathVariable String id) {
        return qrCodeService.downloadQRCodeImageById(id);
    }
}

