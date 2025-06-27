package com.snipsnap.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;
import com.snipsnap.enums.GenerationType;
import com.snipsnap.exception.URLNotFoundException;
import com.snipsnap.model.ShortURL;
import com.snipsnap.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        String shortCode = generateShortCode();

        ShortURL shortURL = new ShortURL();
        shortURL.setShortCode(shortCode);
        shortURL.setOriginalUrl(requestDTO.getOriginalUrl());
        shortURL.setCreatedAt(LocalDateTime.now());

        int expiryDays = (requestDTO.getExpiryDays() != null) ? requestDTO.getExpiryDays() : 1;
        shortURL.setExpiryAt(LocalDateTime.now().plusDays(expiryDays));

        // ✅ Generate QR only if requested
        if (requestDTO.getGenerationType() == GenerationType.QR_CODE_ONLY ||
                requestDTO.getGenerationType() == GenerationType.BOTH) {
            try {
                String fullUrl = baseUrl + "/" + shortCode;
                String qrFilePath = generateAndSaveQRCode(fullUrl, shortCode);
                shortURL.setQrImage(qrFilePath); // Save path, not binary string

            } catch (Exception e) {
                throw new RuntimeException("QR code generation failed");
            }
        }

        ShortURL saved = urlRepository.save(shortURL);

        if (requestDTO.getGenerationType() == GenerationType.SHORT_CODE_ONLY) {
            return new URLResponseDTO(saved.getShortCode(), saved.getOriginalUrl(), null);
        } else if (requestDTO.getGenerationType() == GenerationType.QR_CODE_ONLY) {
            return new URLResponseDTO(null, saved.getOriginalUrl(), "QR Stored in DB");
        } else {
            return new URLResponseDTO(saved.getShortCode(), saved.getOriginalUrl(), "QR Stored in DB");
        }
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

    private String generateAndSaveQRCode(String fullUrl, String shortCode) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(fullUrl, BarcodeFormat.QR_CODE, width, height);

        String folderPath = "qr-codes";
        java.io.File directory = new java.io.File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = folderPath + "/" + shortCode + ".png";
        java.nio.file.Path path = java.nio.file.FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return filePath; // ✅ file path to save in DB
    }

}
