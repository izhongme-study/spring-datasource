package me.izhong.study.springdatasource;

import org.springframework.stereotype.Controller;

@Controller
public class HelloMvc {


    public String sayHello(String name){
        return "hello:" + name;
    }
}
