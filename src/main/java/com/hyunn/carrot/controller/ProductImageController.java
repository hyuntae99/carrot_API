package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.ProductImage;
import com.hyunn.carrot.repository.ProductImageRepository;
import com.hyunn.carrot.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductImageController {

    private final ProductImageService productImageService;
    private final ProductImageRepository productImageRepository;

    @PostMapping("/productImage")
    public Long create(@Valid @RequestBody ProductImage productImage) {
        return productImageService.save(productImage);
    }

    @GetMapping("/productImage/{id}")
    public ProductImage read(@PathVariable Long id) {
        return productImageService.findById(id);
    }

    @GetMapping("/productImages")
    public List<ProductImage> findAll() {
        return productImageRepository.findAll();
    }

    @GetMapping("/productImages/{product-id}")
    public List<ProductImage> findAllByProductId(@PathVariable("product-id") Long product_id) {
        List<ProductImage> productImages = productImageRepository.findAll();
        List<ProductImage> productsImagers_Product = new ArrayList<>(); // 초기화
        for (ProductImage productImage : productImages) {
            if (productImage.getProduct_id() == product_id) {
                productsImagers_Product.add(productImage);
            }
        }
        return productsImagers_Product;
    }

    @PatchMapping("/productImage/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody ProductImage productImage) {
        return productImageService.update(id, productImage);
    }

    @DeleteMapping("/productImage/{id}")
    public Long delete(@PathVariable Long id) {
        productImageService.delete(id);
        return id;
    }
}

