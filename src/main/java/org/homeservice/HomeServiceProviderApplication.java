package org.homeservice;

import org.homeservice.service.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HomeServiceProviderApplication {

    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(HomeServiceProviderApplication.class, args);
        OrderService orderService = run.getBean(OrderService.class);
        OrderService orderService1 = run.getBean(OrderService.class);
        System.out.println();

    }

}