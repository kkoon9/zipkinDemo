package com.example.zipkindemo.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
            long time = System.nanoTime(); // nanoTime도 중복을 발생할 수 있지만 테스트에서는 nanoTime()을 사용
            log.info("{}: controller : {}", time, orderNumber);
            paymentService.payment(orderNumber * 10, time);
            return "OK " + orderNumber;
        }
    }

    @Service
    @Slf4j
    static class BackendPaymentService {
        public void payment(Long price, long time) {
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{}: payment approved : {}" , time, price);
        }
    }
}

