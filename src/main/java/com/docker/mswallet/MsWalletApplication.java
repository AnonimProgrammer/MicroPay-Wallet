package com.docker.mswallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsWalletApplication.class, args);
    }

}
