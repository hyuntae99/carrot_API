package com.hyunn.carrot.repository;

import com.hyunn.carrot.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {

}
