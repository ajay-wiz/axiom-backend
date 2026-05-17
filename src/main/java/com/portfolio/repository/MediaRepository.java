package com.portfolio.repository;

import com.portfolio.entity.Media;
import com.portfolio.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Page<Media> findAll(Pageable pageable);
    Page<Media> findByCategory(Category category, Pageable pageable);
    Page<Media> findByMediaType(Media.MediaType mediaType, Pageable pageable);
    List<Media> findByIsFeaturedTrue();
    List<Media> findByIsTrendingTrue();
    
    @Query("SELECT m FROM Media m WHERE m.title LIKE %:query% OR m.description LIKE %:query% OR m.tags LIKE %:query%")
    Page<Media> searchMedia(@Param("query") String query, Pageable pageable);

    @Modifying
    @Query("UPDATE Media m SET m.viewCount = m.viewCount + 1 WHERE m.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("SELECT COUNT(m) FROM Media m")
    long countAll();
}
