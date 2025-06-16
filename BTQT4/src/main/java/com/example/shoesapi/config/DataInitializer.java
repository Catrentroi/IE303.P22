package com.example.shoesapi.config;

import com.example.shoesapi.model.Product;
import com.example.shoesapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (productRepository.count() == 0) {
            // Initialize with data from product-info.txt (matching your frontend data)
            productRepository.save(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", 
                    "This product is excluded from all promotional discounts and offers", "img1.png"));
            
            productRepository.save(new Product("FORUM MID SHOES", "$100.00", "Adidas", 
                    "This product is excluded from all promotional discounts and offers", "img2.png"));
            
            productRepository.save(new Product("SUPERNOVA SHOES", "$150.00", "Adidas", 
                    "NMD City Stock 2", "img3.png"));
            
            productRepository.save(new Product("BEACH SEASON SHOES", "$160.00", "Adidas", 
                    "Summer collection", "img4.png"));
            
            productRepository.save(new Product("BLACK THADER", "$120.00", "Adidas", 
                    "Speed Run", "img5.png"));
            
            productRepository.save(new Product("FLOWAIR MID SHOES", "$160.00", "Adidas", 
                    "Speed Run", "img6.png"));

            // Additional products to match your frontend (8 products total)
            productRepository.save(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", 
                    "This product is excluded from all promotional discounts and offers", "img1.png"));
            
            productRepository.save(new Product("FORUM MID SHOES", "$100.00", "Adidas", 
                    "This product is excluded from all promotional discounts and offers", "img2.png"));
            
            System.out.println("Sample product data has been initialized!");
        }
    }
}
