package com.hyunn.carrot.service;

import com.hyunn.carrot.entity.Chatting;
import com.hyunn.carrot.repository.ChattingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;

    @Transactional
    public Chatting findById(Long id) {
        return chattingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 목록입니다."));
    }

    @Transactional
    public Long save(Chatting chatting) {
        return chattingRepository.save(chatting).getId();
    }

    @Transactional
    public void delete(Long id) {
        Chatting chatting = findById(id);
        chattingRepository.delete(chatting);
    }

    @Transactional
    public Long update(Long id, Chatting chatting) {
        Chatting currentChatting = findById(id);
        currentChatting.setChatting_name(chatting.getChatting_name());
        return id;
    }

}