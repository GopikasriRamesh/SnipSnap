package com.snipsnap.dto;

import com.snipsnap.enums.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class URLRequestDTO {

    @NotBlank(message = "URL cannot be blank")
    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            message = "Invalid URL format"
    )
    private String originalUrl;

    private Integer expiryDays;

    private String customCode;

    private boolean generateQRCode; // ✅ checkbox state (from UI)

    private GenerationType generationType; // ✅ Optional: could be used instead of generateQRCode if needed

    public URLRequestDTO() {}

    public URLRequestDTO(String originalUrl, Integer expiryDays) {
        this.originalUrl = originalUrl;
        this.expiryDays = expiryDays;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Integer getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public boolean isGenerateQRCode() {
        return generateQRCode;
    }

    public void setGenerateQRCode(boolean generateQRCode) {
        this.generateQRCode = generateQRCode;
    }

    public GenerationType getGenerationType() {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }
}
