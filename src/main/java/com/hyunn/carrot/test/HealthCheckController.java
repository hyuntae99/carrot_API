package com.hyunn.carrot.test;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment environment;

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "현재 실행중이 포트번호는 " + environment.getProperty("local.server.port") + " 입니다.";
    }
}