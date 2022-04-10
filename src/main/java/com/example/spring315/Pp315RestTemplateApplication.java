package com.example.spring315;

import com.example.spring315.service.RestTemplateUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Pp315RestTemplateApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Pp315RestTemplateApplication.class, args);
        RestTemplateUserService restTemplateUserService = ctx.getBean(RestTemplateUserService.class);

        restTemplateUserService.test();



    }

}
