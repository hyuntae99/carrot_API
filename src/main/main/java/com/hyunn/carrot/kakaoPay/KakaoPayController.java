package com.hyunn.carrot.kakaoPay;

import com.hyunn.carrot.kakaoPay.entity.KakaoApproveResponse;
import com.hyunn.carrot.kakaoPay.entity.KakaoCancelResponse;
import com.hyunn.carrot.kakaoPay.entity.KakaoReadyResponse;
import com.hyunn.carrot.kakaoPay.entity.Transaction;
import com.hyunn.carrot.controller.ProductController;
import com.hyunn.carrot.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    private final TransactionRepository transactionRepository;

    private final ProductController productController;

    /**
     * 전체 거래 내역 리스트 확인
     */
    @GetMapping("/list")
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * 회원별 거래 내역 리스트 확인
     */
    @GetMapping("/list/{member-id}")
    public List<Transaction> findAllByMemberId(@PathVariable Long member_id) {
        //판매자일 경우
        List<Product> products_member = productController.findAllByMemberId(member_id);

        // 구매자일 경우
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> transactions_member = new ArrayList<>(); // 초기화
        for (Transaction transaction : transactions) {
            // 판매자 확인
            if (transaction.getBuyer_id() == member_id) {
                transactions_member.add(transaction);
            }
            // 구매자 확인
            for (Product product : products_member) {
                if(transaction.getProduct_id() == product.getId()) {
                    transactions_member.add(transaction);
                }
            }
        }

        return transactions_member;
    }

    /**
     * 결제요청
     */
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay(@Valid @RequestBody Transaction transaction) {

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
