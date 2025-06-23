package com.snipsnap.service;

import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;
import com.snipsnap.exception.URLNotFoundException;
import com.snipsnap.model.ShortURL;
import com.snipsnap.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class URLServiceImpl implements URLService {

    @Autowired
    private URLRepository urlRepository;

    // Shorten the long URL and return short code + original
    @Override
    public URLResponseDTO shortenURL(URLRequestDTO requestDTO) {
        String shortCode;

        if (requestDTO.getCustomCode() != null && !requestDTO.getCustomCode().isEmpty()) {
            // ✅ Use custom code if provided
            if (urlRepository.findByShortCode(requestDTO.getCustomCode()).isPresent()) {
                throw new IllegalArgumentException("Custom short code already exists");
            }
            shortCode = requestDTO.getCustomCode();
        } else {
            // 🔁 Generate random code
            shortCode = generateShortCode();
        }

        ShortURL shortURL = new ShortURL();
        shortURL.setShortCode(shortCode);
        shortURL.setOriginalUrl(requestDTO.getOriginalUrl());
        shortURL.setCreatedAt(LocalDateTime.now());

        int expiryDays = (requestDTO.getExpiryDays() != null) ? requestDTO.getExpiryDays() : 1;
        shortURL.setExpiryAt(LocalDateTime.now().plusDays(expiryDays));

        shortURL.setClickCount(0); // Initialize click count

        ShortURL saved = urlRepository.save(shortURL);

        return new URLResponseDTO(saved.getShortCode(), saved.getOriginalUrl());
    }

    // Resolve short code → original URL and update click count
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

        // Update click count
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    // Short code generator
    private String generateShortCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
