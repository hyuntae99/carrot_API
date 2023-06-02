package com.hyunn.carrot.kakaoPay;

import com.hyunn.carrot.kakaoPay.entity.KakaoApproveResponse;
import com.hyunn.carrot.kakaoPay.entity.KakaoCancelResponse;
import com.hyunn.carrot.kakaoPay.entity.KakaoReadyResponse;
import com.hyunn.carrot.kakaoPay.entity.Transaction;
import com.hyunn.carrot.entity.Member;
import com.hyunn.carrot.entity.Product;
import com.hyunn.carrot.service.MemberService;
import com.hyunn.carrot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction findById(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }


    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "27809591c7e74888a348f6c8ecef80da"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요
    private KakaoReadyResponse kakaoReady;

    private final ProductService productService;

    private final MemberService memberService;


    @Transactional
    public KakaoReadyResponse kakaoPayReady(Transaction transaction) {

        Product product = productService.findById(transaction.getProduct_id());

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid); // 가맹점 코드
        parameters.add("partner_order_id", "partner_order_id"); // 가맹점 주문번호
        parameters.add("partner_user_id", "partner_user_id"); // 가맹점 회원 아이디
        parameters.add("item_name", product.getItem_name()); // 상품명
        parameters.add("quantity", "1"); // 상품 수량
        parameters.add("total_amount", String.valueOf(product.getPrice())); // 상품 총액
        parameters.add("vat_amount", "200"); // 부가세 금액
        parameters.add("tax_free_amount", "0"); // 상품 비과세 금액
        parameters.add("approval_url", "http://localhost:9000/payment/success"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:9000/payment/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:9000/payment/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        // 구매기록에 상품명과 Tid와 구매시간 저장
        transaction.setTid(kakaoReady.getTid());
        transaction.setCreated_at(kakaoReady.getCreated_at());

        return kakaoReady;
    }

    /**
     * 결제 완료 승인
     */
    @Transactional
    public KakaoApproveResponse approveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", "partner_order_id");
        parameters.add("partner_user_id", "partner_user_id");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        // Tid가 같은 구매기록 찾기 (유일한 Tid 값)
        Transaction target_transaction = null;
        List<Transaction> transactionList = transactionRepository.findAll();
        for (Transaction transaction : transactionList){
            if (approveResponse.getTid().equals(transaction.getTid())) {
                target_transaction = transaction;
                break;
            }
        }

        // 상품 구매 처리
        Long product_id = target_transaction.getProduct_id(); // 판매 상품 일련번호
        Product product_sell = productService.findById(product_id); // 판매 상품
        Long seller_id = product_sell.getMember_id(); // 판매자
        Long buyer_id = target_transaction.getBuyer_id(); // 구매자

        // 판매자 잔고 증가
        Member seller = memberService.findById(seller_id);
        seller.setAccount(seller.getAccount() + product_sell.getPrice());

        // 구매자 잔고 감소
        Member buyer = memberService.findById(buyer_id);
        buyer.setAccount(buyer.getAccount() - product_sell.getPrice() + approveResponse.getAmount().getPoint());

        // 판매 처리
        product_sell.setState(1);

        // 구매 기록 (이름, 가격, 포인트)
        target_transaction.setItem_name(product_sell.getItem_name());
        target_transaction.setPrice(product_sell.getPrice());
        target_transaction.setPoint(-1 * approveResponse.getAmount().getPoint());

        return approveResponse;
    }

    /**
     * 결제 환불
     */
    @Transactional
    public KakaoCancelResponse kakaoCancel(Transaction transaction) {

        Product product = productService.findById(transaction.getProduct_id());

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", transaction.getTid());
        parameters.add("cancel_amount", String.valueOf(product.getPrice()));
        parameters.add("cancel_tax_free_amount", "0");
        parameters.add("cancel_vat_amount", "0");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        // 상품 구매 처리
        Long product_id = transaction.getProduct_id(); // 판매 상품 일련번호
        Product product_refund = productService.findById(product_id); // 판매 상품
        Long seller_id = product_refund.getMember_id(); // 판매자
        Long buyer_id = transaction.getBuyer_id(); // 구매자

        // 판매자 잔고 감소
        Member seller = memberService.findById(seller_id);
        seller.setAccount(seller.getAccount() - product_refund.getPrice());

        // 구매자 잔고 증가
        Member buyer = memberService.findById(buyer_id);
        buyer.setAccount(buyer.getAccount() + product_refund.getPrice() - cancelResponse.getAmount().getPoint());

        // 판매 처리 취소
        product_refund.setState(0);

        // 환불된 기록 저장
        Transaction new_transaction = new Transaction();
        new_transaction.update(cancelResponse.getItem_name(),(-1 * cancelResponse.getCanceled_amount().getTotal()),
                cancelResponse.getAmount().getPoint(), product_id, buyer_id, transaction.getTid(), cancelResponse.getCanceled_at());

        transactionRepository.save(new_transaction);

        return cancelResponse;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}