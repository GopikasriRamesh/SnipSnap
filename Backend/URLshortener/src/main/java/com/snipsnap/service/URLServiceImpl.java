package com.snipsnap.service;

import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;
import com.snipsnap.exception.URLNotFoundException;
import com.snipsnap.model.ShortURL;
import com.snipsnap.repository.URLRepository;
import com.snipsnap.util.LinkValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class URLServiceImpl implements URLService {

    @Autowired
    private URLRepository urlRepository;

    @Override
    public URLResponseDTO shortenURL(URLRequestDTO requestDTO) {

        // ✅ Check for spam/malicious URLs
        if (LinkValidator.isMalicious(requestDTO.getOriginalUrl())) {
            throw new IllegalArgumentException("⚠️ Unsafe or spam URL detected. Please use a safe link.");
        }

        // ✅ Check for custom code
        String shortCode;
        if (requestDTO.getCustomCode() != null && !requestDTO.getCustomCode().isEmpty()) {
            if (urlRepository.findByShortCode(requestDTO.getCustomCode()).isPresent()) {
                throw new IllegalArgumentException("Custom short code already exists.");
            }
            shortCode = requestDTO.getCustomCode();
        } else {
            shortCode = generateShortCode();
        }

        ShortURL shortURL = new ShortURL();
        shortURL.setShortCode(shortCode);
        shortURL.setOriginalUrl(requestDTO.getOriginalUrl());
        shortURL.setCreatedAt(LocalDateTime.now());

        int expiryDays = (requestDTO.getExpiryDays() != null) ? requestDTO.getExpiryDays() : 1;
        shortURL.setExpiryAt(LocalDateTime.now().plusDays(expiryDays));
        shortURL.setClickCount(0);

        ShortURL saved = urlRepository.save(shortURL);

        return new URLResponseDTO(saved.getShortCode(), saved.getOriginalUrl());
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
}
