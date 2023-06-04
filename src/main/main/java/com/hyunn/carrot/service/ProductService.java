package com.hyunn.carrot.service;


import com.hyunn.carrot.entity.Product;
import com.hyunn.carrot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    public final ProductRepository productRepository;

    @Transactional
    public Product findById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    @Transactional
    public Long save(Product product){
        return productRepository.save(product).getId();
    }

    @Transactional
    public Long update(Long id, Product product){
        Product currentProduct = findById(id);
        currentProduct.update(product.getTitle(), product.getCategory(), product.getItem_name(), product.getPlace(), product.getPrice(),
                product.getState(), product.getInterest_count(), product.getChatting_count(), product.getContent());
        currentProduct.preUpdate();
        return id;
    }

    @Transactional
    public void delete(Long id){
        Product product = findById(id);
        productRepository.delete(product);
    }
}