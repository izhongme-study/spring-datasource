package me.izhong.study.springdatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class InitService implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("==========system is running:" );

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i=0;i<1;i++) {
            Runnable r = () ->  {
                userService.getUsers();

            };
            executorService.submit(r);
        }
    }
}
