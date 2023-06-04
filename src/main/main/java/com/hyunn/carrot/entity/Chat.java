package com.hyunn.carrot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;


// JPA가 읽어드림
@Entity (name = "chat")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class Chat {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    // 테이블의 column
    private Long id;

    @NotNull(message = "메세지 타입은 필수 입력 값입니다.")
    @Min(value = 0, message = "메세지 타입은 0 또는 1만 입력해주세요.")
    @Max(value = 1, message = "메세지 타입은 0 또는 1만 입력해주세요.")
    private int type;

    private String image_url;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(min= 1, max= 100, message = "내용은 1~100자 사이로 입력해주세요.")
    private String content;

    @NotNull(message = "채팅방 일련번호는 필수 입력 값입니다.")
    @Min(value = 0, message = "채팅방 일련번호는 양수만 입력해주세요.")
    private Long room_id;


}
