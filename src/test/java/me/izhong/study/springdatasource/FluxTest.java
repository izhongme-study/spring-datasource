package me.izhong.study.springdatasource;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class FluxTest {

    @Test
    public void testScheduling() {
        Flux.range(0, 10)
//                .log()    // 1
                .publishOn(Schedulers.newParallel("myParallel"))
                .log()    // 2
                .subscribeOn(Schedulers.newElastic("myElastic"))
                //.log()    // 3
                .blockLast();
    }

}
