package com.hyunn.carrot.repository;

import com.hyunn.carrot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}