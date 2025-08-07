package spring.cloud.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.cloud.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}