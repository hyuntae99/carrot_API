package com.hyunn.carrot.repository;

import com.hyunn.carrot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}