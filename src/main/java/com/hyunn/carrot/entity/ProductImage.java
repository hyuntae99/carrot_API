package com.hyunn.carrot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;


// JPA가 읽어드림
@Entity (name = "product_image")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class ProductImage {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    // 테이블의 column
    private long id;


    @NotBlank(message = "URL은 필수 입력 값입니다.")
    private String url;

    @NotBlank(message = "파일 이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "특수문자가 들어갈 수 없는 값입니다.")
    private String filename;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @PositiveOrZero(message = "양수만 입력해주세요.")
    private long product_id;


}