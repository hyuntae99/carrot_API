package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Chatting;
import com.hyunn.carrot.entity.Product;
import com.hyunn.carrot.repository.ChattingRepository;
import com.hyunn.carrot.service.ChattingService;
import com.hyunn.carrot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChattingService chattingService;
    private final ChattingRepository chattingRepository;
    private final ProductService productService;

    @PostMapping("/chatting")
    public Long create(@Valid @RequestBody Chatting chatting) {

        // 상품 채팅 수 증가
        Long product_id = chatting.getProduct_id();
        Product product = productService.findById(product_id);
        int chatting_count = product.getChatting_count();
        product.setChatting_count(chatting_count+1);
        return chattingService.save(chatting);
    }

    @PatchMapping("/chatting/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody Chatting chatting) {
        return chattingService.update(id, chatting);
    }

    @GetMapping("/chatting/{id}")
    public Chatting read(@PathVariable Long id) {
        return chattingService.findById(id);
    }

    @DeleteMapping("/chatting/{id}")
    public Long delete(@PathVariable Long id) {

        // 상품 채팅 수 감소
        Chatting chatting = chattingService.findById(id);
        Long product_id = chatting.getProduct_id();
        Product product = productService.findById(product_id);
        int chatting_count = product.getChatting_count();
        product.setChatting_count(chatting_count-1);
        chattingService.delete(id);
        return id;
    }

    @GetMapping("/chattings")
    public List<Chatting> findAll() {
        return chattingRepository.findAll();
    }

    @GetMapping("/chattings/{product-id}")
    public List<Chatting> findAllByproductId(@PathVariable("product-id") Long product_id) {
        List<Chatting> chattings = chattingRepository.findAll();
        List<Chatting> chattings_Product = new ArrayList<>(); // 초기화
        for (Chatting chatting : chattings) {
            if (chatting.getProduct_id() == product_id) {
                chattings_Product.add(chatting);
            }
        }
        return chattings_Product;
    }


}