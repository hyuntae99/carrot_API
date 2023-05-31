package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Chat;
import com.hyunn.carrot.entity.Chatting;
import com.hyunn.carrot.entity.Product;
import com.hyunn.carrot.repository.ChatRepository;
import com.hyunn.carrot.service.ChatService;
import com.hyunn.carrot.service.ChattingService;
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
    private final ChattingService chattingService;

    @PostMapping("/chat")
    public Long create(@Valid @RequestBody Chat chat) {
        // 채팅 방 채팅 수 증가
        Chatting chatting = chattingService.findById(chat.getRoom_id());
        int chat_count = chatting.getChat_account();
        chatting.setChat_account(chat_count+1);
        return chatService.save(chat);
    }

    @GetMapping("/chat/{id}")
    public Chat read(@PathVariable Long id) {
        return chatService.findById(id);
    }

    @DeleteMapping("/chat/{id}")
    public Long delete(@PathVariable Long id) {
        // 채팅 방 채팅 수 감소
        Chat chat = chatService.findById(id);
        Chatting chatting = chattingService.findById(chat.getRoom_id());
        int chat_count = chatting.getChat_account();
        chatting.setChat_account(chat_count-1);
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
