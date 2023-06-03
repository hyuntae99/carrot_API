package com.hyunn.carrot.board;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// JPA가 읽어드림
@Entity(name = "board") // 테이블 어노테이션
@Data // lombok 사용 -> 다른 클래스에 참조 가능 (Getter 자동 생성)
public class Board {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql, mariaDB 사용
    private Long id;

    private String title;

    private String category;

    private String item_name;

    private String place;

    private int price;

    private int state = 0;

    private int interest_count = 0;

    private int chatting_count = 0;

    private String content;

    private String filename;

    private String url;

}
