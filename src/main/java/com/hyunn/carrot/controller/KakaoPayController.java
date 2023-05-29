package com.hyunn.carrot.controller;

import com.hyunn.carrot.entity.KakaoPay.KakaoApproveResponse;
import com.hyunn.carrot.entity.KakaoPay.KakaoCancelResponse;
import com.hyunn.carrot.entity.KakaoPay.KakaoReadyResponse;
import com.hyunn.carrot.entity.KakaoPay.Transaction;
import com.hyunn.carrot.repository.TransactionRepository;
import com.hyunn.carrot.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    private final TransactionRepository transactionRepository;

    /**
     * 거래 내역 리스트 확인
     */
    @GetMapping("/list")
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * 결제요청
     */
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay(@RequestBody Transaction transaction) {

        kakaoPayService.save(transaction);
        return kakaoPayService.kakaoPayReady(transaction);
    }

    /**
     * 결제 성공
     */
    @GetMapping("/success")
    public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public String cancel() {

        // 리다이렉션 필요 (웹 페이지 만들기)
        System.out.println("결제 진행 중 취소");
        return "결제 진행 중 취소";
    }


    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public String fail() {

        // 리다이렉션 필요 (웹 페이지 만들기)
        System.out.println("결제 실패");
        return "결제 실패";

    }

    /**
     * 환불
     */
    @PostMapping("/refund/{id}")
    public ResponseEntity refund(@PathVariable Long id) {

        Transaction transaction = kakaoPayService.findById(id);

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(transaction);

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}
