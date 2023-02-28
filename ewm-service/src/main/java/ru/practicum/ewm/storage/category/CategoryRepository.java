package ru.practicum.ewm.storage.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
