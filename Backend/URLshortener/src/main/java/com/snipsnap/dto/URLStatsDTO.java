package com.snipsnap.dto;

import java.time.LocalDateTime;

public class URLStatsDTO {
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private int clickCount;

    public URLStatsDTO() {}

    public URLStatsDTO(String originalUrl, String shortCode, LocalDateTime createdAt,
                       LocalDateTime expiryAt, int clickCount) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
        this.clickCount = clickCount;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
