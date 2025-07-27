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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;


@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class URLController {

    @Autowired
    private URLService urlService;

    @Autowired
    private URLRepository urlRepository;

   @PostMapping("/shorten")
public ResponseEntity<URLResponseDTO> createShortURL(@RequestBody @Valid URLRequestDTO requestDTO) {
    URLResponseDTO responseDTO = urlService.shortenURL(requestDTO);
    return ResponseEntity.ok(responseDTO);
}

 @GetMapping("/{shortCode}")
public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode) {
    String originalUrl = urlService.getOriginalURL(shortCode);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(originalUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
}
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
