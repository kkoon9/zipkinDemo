package com.example.zipkindemo.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        /**
         * 아래 SpringApplication 내에 있는 run 메서드가 인자로 args를 받을 수 있으므로
         * 이 코드는 주석 처리하였다.
         */
        // System.setProperty("spring.application.name", "backend");
        SpringApplication.run(BackendApplication.class, "--spring.application.name=backend", "--server.port=8081");
    }

    @RestController
    @Slf4j
    static class BackendController {
        @Autowired
        private BackendPaymentService paymentService;

        @GetMapping("/order/{orderNumber}")
        public String order(@PathVariable Long orderNumber) {
            log.info("{}: controller", orderNumber);
            paymentService.payment(orderNumber * 10);
            return "OK " + orderNumber;
        }
    }

    @Service
    @Slf4j
    static class BackendPaymentService {
        @NewSpan(name = "backendPayment") // 색인의 역할을 한다.
        public void payment(@SpanTag("payment-price") Long price) {
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{}: payment approved", price);
        }
    }
}

