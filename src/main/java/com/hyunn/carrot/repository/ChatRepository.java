package com.hyunn.carrot.repository;

import com.hyunn.carrot.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}