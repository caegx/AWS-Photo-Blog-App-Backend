package spring.cloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}