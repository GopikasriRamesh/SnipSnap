// ✅ ShortURL.java
package com.snipsnap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "short_url")
public class ShortURL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String originalUrl;

    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;

    private int clickCount = 0;

    @Lob
    @Column(name = "qr_image", columnDefinition = "TEXT")
    private String qrImage;  // ✅ New field to store QR image in DB

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiryAt() { return expiryAt; }
    public void setExpiryAt(LocalDateTime expiryAt) { this.expiryAt = expiryAt; }

    public int getClickCount() { return clickCount; }
    public void setClickCount(int clickCount) { this.clickCount = clickCount; }

    public String getQrImage() { return qrImage; }
    public void setQrImage(String qrImage) { this.qrImage = qrImage; }
}