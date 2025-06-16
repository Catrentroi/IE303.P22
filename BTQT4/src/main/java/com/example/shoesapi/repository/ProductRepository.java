package com.example.shoesapi.repository;

import com.example.shoesapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by brand
    List<Product> findByBrandIgnoreCase(String brand);
    
    // Find products by name containing keyword
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // Custom query to find products by price range
    @Query("SELECT p FROM Product p WHERE CAST(REPLACE(REPLACE(p.price, '$', ''), ',', '') AS double) BETWEEN ?1 AND ?2")
    List<Product> findByPriceRange(double minPrice, double maxPrice);
}
