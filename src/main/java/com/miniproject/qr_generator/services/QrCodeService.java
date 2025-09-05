package com.miniproject.qr_generator.services;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

@Service
public class QrCodeService {

    public byte[] generateQrWithLogo(String text, InputStream logoStream, int width, int height) throws Exception {
        // Encode content into QR
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");   // support emoji/unicode
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // high level, allows logo overlay

        BitMatrix matrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        // Create QR code image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, new MatrixToImageConfig());

        // ✅ If logo is not provided, return plain QR
        if (logoStream == null) {
            return bufferedImageToBytes(qrImage);
        }

        // Try reading logo
        BufferedImage logo = ImageIO.read(logoStream);
        if (logo == null) {
            return bufferedImageToBytes(qrImage); // fallback: return plain QR
        }

        // Scale logo
        int logoWidth = qrImage.getWidth() / 5;
        int logoHeight = qrImage.getHeight() / 5;

        // Position logo at center
        int x = (qrImage.getWidth() - logoWidth) / 2;
        int y = (qrImage.getHeight() - logoHeight) / 2;

        // Draw logo on QR
        Graphics2D g = qrImage.createGraphics();
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(logo, x, y, logoWidth, logoHeight, null);
        g.dispose();

        return bufferedImageToBytes(qrImage);
    }

    private byte[] bufferedImageToBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
