package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Chatting;
import com.hyunn.carrot.repository.ChattingRepository;
import com.hyunn.carrot.service.ChattingService;
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

    @PostMapping("/chatting")
    public Long create(@Valid @RequestBody Chatting interest) {
        return chattingService.save(interest);
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


    @GetMapping("/chattings/count")
    public Long countByProductId(@RequestParam("product-id") Long product_id) {
        long count = 0;
        List<Chatting> chattings = chattingRepository.findAll();
        for (Chatting chatting : chattings) {
            if (chatting.getProduct_id() == product_id) {
                count++;
            }
        }
        return count;
    }

}