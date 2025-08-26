package spring.cloud.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findByUser_IdAndDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Image> findByUser_IdAndDeletedTrueOrderByUpdatedAtDesc(Long id, Pageable pageable);
}
