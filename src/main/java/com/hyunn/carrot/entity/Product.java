package com.hyunn.carrot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.*;

// JPA가 읽어드림
@Entity(name = "product")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
@EqualsAndHashCode(callSuper = true) // 상위 클래스까지 반영
public class Product extends BaseTimeEntity {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용

    // 테이블의 column
    private long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 5, max = 30, message = "제목은 5글자 이상 30글자 이내로 입력해주세요.")
    private String title;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "상품 이름은 필수 입력 값입니다.")
    private String item_name;

    @NotBlank(message = "장소는 필수 입력 값입니다.")
    private String place;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Min(value = 500, message = "가격은 500 이상으로 입력해주세요.")
    private int price;

    private int state = 0;

    private int interest_count = 0;

    private int chatting_count = 0;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(min = 5, max = 100, message = "내용은 5글자 이상 100글자 이내로 입력해주세요.")
    private String content;

    @NotNull(message = "판매자 일련번호는 필수 입력 값입니다.")
    @Min(value = 0, message = "판매자 일련번호는 양수만 입력해주세요.")
    private long member_id;


    public void update(String title, String category, String item_name, String place, int price, int state,
                       int interest_count, int chatting_count, String content){
        this.title = title;
        this.category = category;
        this.item_name = item_name;
        this.place = place;
        this.price = price;
        this.state = state;
        this.interest_count = interest_count;
        this.chatting_count = chatting_count;
        this.content = content;
    }

    // 관심 수, 채팅방 수 메소드 만들기


}
