package com.miniproject.qr_generator.controller;

import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.qr_generator.dto.QrRequest;
import com.miniproject.qr_generator.services.QrCodeService;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateQr(@RequestBody QrRequest request) {
        try {
            // Build vCard string (without email)
            String vCard = "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "FN:" + request.getName() + "\n" +
                    "TEL:" + request.getContactNumber() + "\n" +
                    "NOTE:Bike Number: " + request.getBikeNumber() + "\n" +
                    "END:VCARD";

            // Load logo (optional, for center image)
            InputStream logoStream = getClass().getResourceAsStream("/static/logo.png");

            byte[] qrImage = qrCodeService.generateQrWithLogo(vCard, logoStream, 400, 400);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}


