package com.miniproject.qr_generator.controller;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.qr_generator.dto.QrRequest;
import com.miniproject.qr_generator.services.QrCodeService;
import com.miniproject.qr_generator.services.QrLinkService;
import com.miniproject.qr_generator.services.ScanLimiterService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    private final ScanLimiterService scanLimiterService;
    
    private QrLinkService qrLinkService;

    public QrCodeController(QrCodeService qrCodeService, ScanLimiterService scanLimiterService, QrLinkService qrLinkService) {
        this.qrCodeService = qrCodeService;
        this.scanLimiterService = scanLimiterService;
        this.qrLinkService = qrLinkService;
    }


    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateQr(@RequestBody QrRequest request) {
        try {
            String qrContent =
                "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "FN:" + request.getName() + "\n" +
                "TEL;TYPE=CELL:" + request.getContactNumber() + "\n" +
                "NOTE:Bike Number - " + request.getBikeNumber() + "\n" +
                "END:VCARD";

            byte[] qrImage = qrCodeService.generateQr(qrContent, 400, 400);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // 🔔 New method for WhatsApp alert QR
    @PostMapping("/generate-whatsapp-alert")
    public ResponseEntity<byte[]> generateWhatsappQr(@RequestBody QrRequest request,
                                                     HttpServletRequest servletRequest) {
        try {
            String userId = servletRequest.getRemoteAddr(); // Use IP as identifier

            if (!scanLimiterService.canSend(userId)) {
                return ResponseEntity.status(429)
                        .body(("Limit exceeded. Only 2 messages allowed per day.").getBytes());
            }

            String phone = request.getContactNumber().replaceAll("[^\\d]", "");
            String message = "The vehicle number " + request.getBikeNumber() +
                             " is blocking my pathway. Please take it away.\n\n" +
                             "— Sent via QR Alert System by Theeran";

            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String whatsappUrl = "https://wa.me/" + phone + "?text=" + encodedMessage;

            byte[] qrImage = qrCodeService.generateQr(whatsappUrl, 400, 400);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=whatsapp_qr.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/send-message")
    public ResponseEntity<Void> redirectToWhatsapp(@RequestParam String id) {
        QrRequest request = qrLinkService.getRequest(id);

        if (request == null) {
            return ResponseEntity.status(404).build();
        }

        String phone = request.getContactNumber().replaceAll("[^\\d]", "");
        String message = "The vehicle number " + request.getBikeNumber() +
                         " is blocking my pathway. Please take it away.\n\n" +
                         "— Sent via QR Alert System by Theeran";

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        URI whatsappUri = URI.create("https://wa.me/" + phone + "?text=" + encodedMessage);

        return ResponseEntity.status(302).location(whatsappUri).build();
    }
    @PostMapping("/generate-safe-whatsapp")
    public ResponseEntity<byte[]> generateSafeWhatsappQr(@RequestBody QrRequest request) {
        try {
            String id = qrLinkService.register(request);
            String qrContent = "https://qr-generator-api-iyqz.onrender.com/api/qrcode/send-message?id=" + id;

            byte[] qrImage = qrCodeService.generateQr(qrContent, 400, 400);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=safe_whatsapp_qr.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }



}
