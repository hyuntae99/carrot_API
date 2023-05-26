package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Interest;
import com.hyunn.carrot.repository.InterestRepository;
import com.hyunn.carrot.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class InterestController {

    private final InterestService interestService;
    private final InterestRepository interestRepository;

    @PostMapping("/interest")
    public Long create(@Valid @RequestBody Interest interest) {
        return interestService.save(interest);
    }

    @GetMapping("/interest/{id}")
    public Interest read(@PathVariable Long id) {
        return interestService.findById(id);
    }

    @DeleteMapping("/interest/{id}")
    public Long delete(@PathVariable Long id) {
        interestService.delete(id);
        return id;
    }

    @GetMapping("/interests")
    public List<Interest> findAll() {
        return interestRepository.findAll();
    }

    @GetMapping("/interests/count")
    public Long countByProductId(@RequestParam("product-id") Long product_id) {
        long count = 0;
        List<Interest> interests = interestRepository.findAll();
        for (Interest interest : interests) {
            if (interest.getProduct_id() == product_id) {
                count++;
            }
        }
        return count;
    }


    @GetMapping("/interests/{member-id}")
    public List<Interest> findAllByMemberId(@PathVariable("member-id") Long member_id) {
        List<Interest> interests = interestRepository.findAll();
        List<Interest> interests_Member = new ArrayList<>(); // 초기화
        for (Interest interest : interests) {
            if (interest.getMember_id() == member_id) {
                interests_Member.add(interest);
            }
        }
        return interests_Member;
    }
}


