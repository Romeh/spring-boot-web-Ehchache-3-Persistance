package com.demo.alertmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class AlertManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(AlertManagerApplication.class, args);

    }
}
