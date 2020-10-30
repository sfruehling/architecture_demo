package com.example.demo.frontend;

import com.example.demo.facade.HelloFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private HelloFacade helloFacade;

    HomeController(HelloFacade helloFacade){
        this.helloFacade = helloFacade;
    }

    @GetMapping("/")
    String hello(){
        return helloFacade.sayHello();
    }
}
