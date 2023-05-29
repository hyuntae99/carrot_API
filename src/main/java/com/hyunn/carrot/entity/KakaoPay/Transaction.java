package com.hyunn.carrot.entity.KakaoPay;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity(name = "transaction")
@Data
public class Transaction {
    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    private Long id;

    @NotNull
    @Max(value = 2100000000, message = "Integer 범위 내에서만 입력해주세요.")
    @Min(value = -2100000000, message = "Integer 범위 내에서만 입력해주세요.")
    private int price;

    @NotNull
    @Min(value = 0, message = "상품 일련번호는 양수만 입력해주세요.")
    private Long product_id;

    @NotNull
    @Min(value = 0, message = "구매자 일련번호는 양수만 입력해주세요.")
    private Long buyer_id;

    private String tid = null; // 결제 고유 번호

    private String created_at = null; // 결제 생성 시간

    private String canceled_at = null; // 환불 시간

    // 환불 기록 업데이트
    public void update(int price, Long product_id, Long buyer_id, String tid, String canceled_at) {
        this.price = price;
        this.product_id = product_id;
        this.buyer_id = buyer_id;
        this.tid = tid;
        this.canceled_at = canceled_at;
    }

}
