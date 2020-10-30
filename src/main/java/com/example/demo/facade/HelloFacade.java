package com.example.demo.facade;

import com.example.demo.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloFacade {
    private HelloService helloService;

    public HelloFacade(HelloService helloService) {
        this.helloService = helloService;
    }

    public String sayHello() {
        return helloService.sayServiceHallo();
    }
}
