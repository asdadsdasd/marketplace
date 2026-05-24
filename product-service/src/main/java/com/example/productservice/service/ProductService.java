package com.example.productservice.service;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional()
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setTitle(request.title());
        product.setStock(request.stock());
        product.setPrice(request.price());
        product.setDescription(request.description());

        productRepository.save(product);

        return ProductResponse.fromEntity(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {

        return productRepository
                .findAll(pageable)
                .map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        return ProductResponse.fromEntity(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
