package pe.edu.upc.fixcampus.repository;

import pe.edu.upc.fixcampus.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
