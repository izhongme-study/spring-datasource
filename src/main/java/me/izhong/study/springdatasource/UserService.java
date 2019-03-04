package me.izhong.study.springdatasource;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService implements IUserService{

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//timeout = 15,
    @Transactional(timeout = 15,propagation = Propagation.REQUIRED)
    public void getUsers(){

        jdbcTemplate.setQueryTimeout(10);
        jdbcTemplate.update("update  users set name='xx' where id='1' ");
        try {
            log.info("sleep 5");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
