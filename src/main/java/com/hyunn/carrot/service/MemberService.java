package com.hyunn.carrot.service;

import com.hyunn.carrot.entity.Member;
import com.hyunn.carrot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    public final MemberRepository memberRepository;

    @Transactional
    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public Long save(Member member){
        return memberRepository.save(member).getId();
    }

    @Transactional
    public Long update(Long id, Member member){
        Member currentUser = findById(id);
        currentUser.update(member.getEmail(), member.getPassword(), member.getName(), member.getNickname(),
                member.getPhone_num(), member.getProfile(), member.getManner_temp(), member.getProfile_url(), member.getAccount());
        return id;
    }

    @Transactional
    public void delete(Long id){
        Member member = findById(id);
        memberRepository.delete(member);
    }


}