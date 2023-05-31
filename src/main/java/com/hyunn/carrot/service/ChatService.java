package com.hyunn.carrot.service;

import com.hyunn.carrot.entity.Chat;
import com.hyunn.carrot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public Chat findById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 목록입니다."));
    }

    @Transactional
    public Long save(Chat chatting) {
        return chatRepository.save(chatting).getId();
    }

    @Transactional
    public void delete(Long id) {
        Chat chatting = findById(id);
        chatRepository.delete(chatting);
    }

}
