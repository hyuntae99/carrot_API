package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.Member;
import com.hyunn.carrot.repository.MemberRepository;
import com.hyunn.carrot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @PostMapping("/member")
    public Long create(@Valid @RequestBody Member member) {
        return memberService.save(member);
    }

    @GetMapping("/member/{id}")
    public Member read(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("/members")
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @PatchMapping("/member/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody Member member) {
        return memberService.update(id, member);
    }

    @DeleteMapping("/member/{id}")
    public Long delete(@PathVariable Long id) {
        memberService.delete(id);
        return id;
    }
}
