package me.izhong.study.springdatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class SpringDatasourceApplication {

    private static ExecutorService executorService = Executors.newFixedThreadPool(200);
    private static transient boolean run = true;
    @Autowired
    private DataSource dataSource;

    private AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        SpringApplication.run(SpringDatasourceApplication.class, args);
    }

    @PostConstruct
    public void testInsert() {

        Runnable tpsr = () -> {
            int last = 0;
            while (run) {
                int current = count.get();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int tps = (current - last) / 3;
                System.out.println("tps:" + tps);
                last = current;
            }
        };

        executorService.submit(tpsr);

        System.out.println("dataSource is:" + dataSource.getClass().getName());
        for (int i = 0; i < 100; i++) {
            Runnable c1 = () -> {
                while (run) {
                    Connection connection = null;
                    Statement ps = null;
                    try {
                        connection = dataSource.getConnection();
                        //System.out.println("autocommit:" + connection.getAutoCommit());
                        //System.out.println("TransactionIsolation:" + connection.getTransactionIsolation());
                        connection.setAutoCommit(false);
                        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                        //ps = connection.prepareStatement("insert into users values (?,?,?,?)");

                        ps = connection.createStatement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        int id = count.getAndIncrement();
                        if (ps == null) {
                            System.out.println("ps is null " + Thread.currentThread().getName());
                            break;
                        }
                        //ps.setInt(1, id);
                        //ps.setString(2, "xiaomin11");
                        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
                        //ps.setTimestamp(3, ts);
                        //ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        int ct = ps.executeUpdate("insert into users values" +
                                " (" + id + "," + "\'xiaomin11\'" + ",\'" + ts.toString() + "\',\'" + ts.toString() + "\')");
                        if (id % 10000 == 0) {
                            //System.out.println("count:" + id);
                        }
                        ps.close();
                        connection.commit();
                        //ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {


                    }
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            };
            System.out.println("connection established " + i);
            executorService.submit(c1);
        }


        try {
            Thread.sleep(180000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        run = false;
        System.out.println("start down...:");
        executorService.shutdownNow();
        System.out.println("total count:" + count.get());
    }


    @ConditionalOnProperty("dhcp")
    @Bean
    public DataSource dataSourceDhcp() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://10.10.51.212:13306/test?characterEncoding=utf8&useSSL=false");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setMaxActive(600);
        return dataSource;
    }

    @ConditionalOnProperty("c3p0")
    @Bean
    public DataSource dataSourceC3P0() throws Exception {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://10.10.51.212:13306/test?characterEncoding=utf8&useSSL=false");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setUser("root");
        dataSource.setPassword("password");
        dataSource.setMaxPoolSize(600);
        return dataSource;
    }

    @ConditionalOnProperty("druid")
    @Bean
    public DataSource dataSourceDruid() throws Exception {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://10.10.51.212:13306/test?characterEncoding=utf8&useSSL=false");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setMaxActive(600);
        return dataSource;
    }


    @ConditionalOnProperty("hikari")
    @Bean
    public DataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://10.10.51.212:13306/test?characterEncoding=utf8&useSSL=false");
        //props.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        props.setProperty("username", "root");
        props.setProperty("password", "password");
        //props.setProperty("databaseName", "dbtest");
        //props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        config.setMaximumPoolSize(600);
        config.setConnectionTimeout(17000);
        HikariDataSource ds = new HikariDataSource(config);

        return ds;
    }
}
