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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // ✅ Save the short URL to DB first
    ShortURL saved = urlRepository.save(shortURL);

    // ✅ Generate QR and update DB if requested
    if (requestDTO.isGenerateQRCode()) {
        try {
            String fullUrl = baseUrl + "/" + saved.getShortCode();
            String qrBase64 = generateQRCodeBase64(fullUrl);

            saved.setQrImage(qrBase64);
            urlRepository.save(saved);
        } catch (Exception e) {
            throw new RuntimeException("QR code generation failed: " + e.getMessage());
        }
    }

    // ✅ Return response including QR
    return new URLResponseDTO(
            saved.getShortCode(),
            saved.getOriginalUrl(),
            saved.getQrImage()
    );
}


    @Override
    public String getOriginalURL(String shortCode) {
        ShortURL shortURL = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new URLNotFoundException("Short URL not found: " + shortCode));

        if (shortURL.getExpiryAt() != null && LocalDateTime.now().isAfter(shortURL.getExpiryAt())) {
            throw new URLNotFoundException("Short URL has expired.");
        }

        shortURL.setClickCount(shortURL.getClickCount() + 1);
        urlRepository.save(shortURL);

        return shortURL.getOriginalUrl();
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateQRCodeBase64(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}
