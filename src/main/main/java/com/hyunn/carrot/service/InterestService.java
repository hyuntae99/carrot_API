package com.hyunn.carrot.service;

import com.hyunn.carrot.entity.Interest;
import com.hyunn.carrot.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    @Transactional
    public Interest findById(Long id) {
        return interestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 목록입니다."));
    }

    @Transactional
    public Long save(Interest interest) {
        return interestRepository.save(interest).getId();
    }

    @Transactional
    public void delete(Long id) {
        Interest interest = findById(id);
        interestRepository.delete(interest);
    }
}
