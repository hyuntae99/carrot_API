package com.hyunn.carrot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// JPA가 읽어드림
@Entity (name = "chatting")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class Chatting {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    // 테이블의 column
    private Long id;

    @Size(min= 2, max= 10, message = "채팅방 이름은 2~10자 사이로 입력해주세요.")
    private String chatting_name = null;

    @NotNull(message = "상품 일련번호는 필수 입력 값입니다.")
    @Min(value = 0, message = "상품 일련번호는 양수만 입력해주세요.")
    private Long product_id;

    @NotNull(message = "구매자 일련번호는 필수 입력 값입니다.")
    @Min(value = 0, message = " 구매자 일련번호는 양수만 입력해주세요.")
    private Long buyer_id;

    private int chat_account = 0;


}