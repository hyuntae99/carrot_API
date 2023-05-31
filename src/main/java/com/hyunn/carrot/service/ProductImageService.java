package com.hyunn.carrot.service;

import com.hyunn.carrot.entity.ProductImage;
import com.hyunn.carrot.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductImage findById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사진입니다."));
    }

    @Transactional
    public Long save(ProductImage productImage) {
        return productImageRepository.save(productImage).getId();
    }

    @Transactional
    public Long update(Long id, ProductImage productImage) {
        ProductImage currentProductImage = findById(id);
        currentProductImage.setUrl(productImage.getUrl());
        currentProductImage.setFilename(productImage.getFilename());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        ProductImage productImage = findById(id);
        productImageRepository.delete(productImage);
    }


}
