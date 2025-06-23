package com.snipsnap.service;

import com.snipsnap.dto.URLRequestDTO;
import com.snipsnap.dto.URLResponseDTO;

public interface URLService {
    URLResponseDTO shortenURL(URLRequestDTO requestDTO);
    String getOriginalURL(String shortCode);
}
