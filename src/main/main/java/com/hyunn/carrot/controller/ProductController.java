package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Product;
import com.hyunn.carrot.repository.ProductRepository;
import com.hyunn.carrot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("/product")
    public Long create(@Valid @RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/product/{id}")
    public Product read(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/products")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/products/available")
    public List<Product> findAllByState() {
        List<Product> products = productRepository.findAll();
        List<Product> products_available= new ArrayList<>(); // 초기화
        for (Product product : products) {
            if (product.getState() == 0) {
                products_available.add(product);
            }
        }
        return products_available;
    }

    @GetMapping("/products/{member-id}")
    public List<Product> findAllByMemberId(@PathVariable("member-id") Long member_id) {
        List<Product> products = productRepository.findAll();
        List<Product> products_Member = new ArrayList<>(); // 초기화
        for (Product product : products) {
            if (product.getMember_id() == member_id) {
                products_Member.add(product);
            }
        }
        return products_Member;
    }


    @PatchMapping("/product/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody Product product){
        return productService.update(id, product);
    }

    @DeleteMapping("/product/{id}")
    public Long delete(@PathVariable Long id){
        productService.delete(id);
        return id;
    }
}
