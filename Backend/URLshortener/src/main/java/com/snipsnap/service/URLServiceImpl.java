// src/main/java/com/snipsnap/service/URLServiceImpl.java
package com.snipsnap.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;
import com.snipsnap.exception.URLNotFoundException;
import com.snipsnap.model.ShortURL;
import com.snipsnap.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class URLServiceImpl implements URLService {

    @Autowired
    private URLRepository urlRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public URLResponseDTO shortenURL(URLRequestDTO requestDTO) {
        String shortCode = (requestDTO.getCustomCode() != null && !requestDTO.getCustomCode().isEmpty())
                ? requestDTO.getCustomCode()
                : generateShortCode();

        if (urlRepository.findByShortCode(shortCode).isPresent()) {
            throw new RuntimeException("Custom short code already exists: " + shortCode);
        }

        ShortURL shortURL = new ShortURL();
        shortURL.setShortCode(shortCode);
        shortURL.setOriginalUrl(requestDTO.getOriginalUrl());
        shortURL.setCreatedAt(LocalDateTime.now());

        int expiryDays = (requestDTO.getExpiryDays() != null) ? requestDTO.getExpiryDays() : 1;
        shortURL.setExpiryAt(LocalDateTime.now().plusDays(expiryDays));

        // ✅ Generate QR Code
        if (requestDTO.isGenerateQRCode()) {
            try {
                String fullUrl = baseUrl + "/" + shortCode;
                String qrBase64 = generateQRCodeBase64(fullUrl);
                shortURL.setQrImage(qrBase64);
            } catch (Exception e) {
                throw new RuntimeException("QR code generation failed: " + e.getMessage());
            }
        }

        ShortURL saved = urlRepository.save(shortURL);

        return new URLResponseDTO(
                saved.getShortCode(),
                saved.getOriginalUrl(),
                saved.getQrImage()
        );
    }

    @Override
    public String getOriginalURL(String shortCode) {
        Optional<ShortURL> result = urlRepository.findByShortCode(shortCode);

        if (result.isEmpty()) {
            throw new URLNotFoundException("Short URL not found: " + shortCode);
        }

        ShortURL url = result.get();

        if (url.getExpiryAt() != null && LocalDateTime.now().isAfter(url.getExpiryAt())) {
            throw new URLNotFoundException("Short URL has expired.");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateQRCodeBase64(String text) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrBytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(qrBytes);
    }
}
