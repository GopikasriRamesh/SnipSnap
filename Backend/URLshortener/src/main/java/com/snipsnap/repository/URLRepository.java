package com.snipsnap.repository;

import com.snipsnap.model.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<ShortURL, Long> {
    Optional<ShortURL> findByShortCode(String shortCode);
}
