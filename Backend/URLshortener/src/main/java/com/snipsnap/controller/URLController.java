package com.snipsnap.controller;

import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;
import com.snipsnap.dto.URLStatsDTO;
import com.snipsnap.exception.URLNotFoundException;
import com.snipsnap.model.ShortURL;
import com.snipsnap.repository.URLRepository;
import com.snipsnap.service.URLService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class URLController {

    @Autowired
    private URLService urlService;

    @Autowired
    private URLRepository urlRepository;

    // ✅ POST /shorten — Create a short URL
    @PostMapping("/shorten")
    public ResponseEntity<URLResponseDTO> createShortURL(
            @RequestBody @Valid URLRequestDTO requestDTO) {
        URLResponseDTO responseDTO = urlService.shortenURL(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // ✅ GET /{shortCode} — Redirect to the original URL
    @GetMapping("/{shortCode}")
    public RedirectView redirectToOriginal(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalURL(shortCode);
        return new RedirectView(originalUrl);
    }

    // ✅ GET /stats/{shortCode} — Get stats like creation time, expiry, click count
    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<URLStatsDTO> getURLStats(@PathVariable String shortCode) {
        ShortURL url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new URLNotFoundException("Short URL not found"));

        URLStatsDTO stats = new URLStatsDTO(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt(),
                url.getExpiryAt(),
                url.getClickCount()
        );

        return ResponseEntity.ok(stats);
    }
}
