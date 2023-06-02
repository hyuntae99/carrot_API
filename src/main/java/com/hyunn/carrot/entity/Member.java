package com.hyunn.carrot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

// JPA가 읽어드림
@Entity (name = "member")// 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class Member {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    // 테이블의 column
    private Long id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{1,15}$", message = "이름은 특수문자를 제외한 1~15자리여야 합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{1,15}$", message = "닉네임은 특수문자를 제외한 1~15자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "번호는 필수 입력 값입니다.")
    @PositiveOrZero(message = "번호(숫자)만 입력해주세요.")
    private String phone_num;

    @NotBlank(message = "프로필 내용은 필수 입력 값입니다.")
    @Size(min= 10, max= 100, message = "프로필 내용은 10~100자 사이로 입력해주세요.")
    private String profile;

    @Min(value = 0, message = "온도는 0도가 최솟값입니다.")
    private int manner_temp = 36;

    private LocalDateTime create_date = LocalDateTime.now();

    private String profile_url = null;

    @Max(value = 2100000000, message = "Integer 범위 내에서만 입력해주세요.")
    @Min(value = -2100000000, message = "Integer 범위 내에서만 입력해주세요.")
    private int account = 0;

    private String  site = "일반 회원";

    // 회원 정보 업데이트
    public void update(String email, String password, String name, String nickname,
                       String phone_num, String profile, int manner_temp, String profile_url, int account){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone_num = phone_num;
        this.profile = profile;
        this.manner_temp = manner_temp;
        this.profile_url = profile_url;
        this.account = account;
    }

}
