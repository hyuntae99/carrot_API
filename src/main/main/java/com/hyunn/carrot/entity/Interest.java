package com.hyunn.carrot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

// JPA가 읽어드림
@Entity (name = "interest")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class Interest {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    // 테이블의 column
    private Long id;

    @NotNull(message = "상품 번호는 필수 입력 값입니다.")
    @Min(value = 0, message = "상품 번호는 양수만 입력해주세요.")
    private Long product_id;

    @NotNull(message = "회원 번호는 필수 입력 값입니다.")
    @Min(value = 0, message = "회원 번호는 양수만 입력해주세요.")
    private Long member_id;


}
