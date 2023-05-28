package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Chat;
import com.hyunn.carrot.repository.ChatRepository;
import com.hyunn.carrot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @PostMapping("/chat")
    public Long create(@Valid @RequestBody Chat chat) {
        return chatService.save(chat);
    }

    @GetMapping("/chat/{id}")
    public Chat read(@PathVariable Long id) {
        return chatService.findById(id);
    }

    @DeleteMapping("/chat/{id}")
    public Long delete(@PathVariable Long id) {
        chatService.delete(id);
        return id;
    }

    @GetMapping("/chats/{room-id}")
    public List<Chat> findAllByRoomId(@PathVariable("room-id") Long room_id) {
        List<Chat> chats = chatRepository.findAll();
        List<Chat> chats_room = new ArrayList<>(); // 초기화
        for (Chat chat : chats) {
            if (chat.getRoom_id() == room_id) {
                chats_room.add(chat);
            }
        }
        return chats_room;
    }


}
