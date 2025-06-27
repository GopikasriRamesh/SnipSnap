package com.snipsnap.dto;

public class URLResponseDTO {
    private String shortCode;
    private String originalUrl;
    private String qrPath; // ✅ optional

    public URLResponseDTO(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

    public URLResponseDTO(String shortCode, String originalUrl, String qrPath) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.qrPath = qrPath;
    }

    // ✅ Getters and Setters
    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath;
    }
}
