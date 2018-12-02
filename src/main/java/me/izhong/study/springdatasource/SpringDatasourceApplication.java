package me.izhong.study.springdatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.codahale.metrics.*;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringDatasourceApplication {

//    private static ExecutorService executorService = Executors.newFixedThreadPool(200);
    private static transient boolean run = true;
//    @Autowired
//    private DataSource dataSource;

//    private AtomicInteger count = new AtomicInteger(0);
//    private static MetricRegistry metrics = new MetricRegistry();
//    private static HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    public static void main(String[] args) {
        SpringApplication.run(SpringDatasourceApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.setPort(3300);
        tomcatServletWebServerFactory.setBaseDirectory(new File("/"));
        return tomcatServletWebServerFactory;
    }

    @PostConstruct
    public void testPost(){
        System.out.println("slog done:");
    }

    //@PostConstruct
    public void testInsert() {

       /* Runnable tpsr = () -> {
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


//                final Map<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
//                for (Map.Entry<String, HealthCheck.Result> entry : results.entrySet()) {
//                    if (entry.getValue().isHealthy()) {
//                        System.out.println(entry.getKey() + " is healthy");
//                    } else {
//                        System.err.println(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
//                        final Throwable e = entry.getValue().getError();
//                        if (e != null) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

//                SortedMap<String, Gauge> gauges = metrics.getGauges();
//                gauges.forEach( (k,v) -> {
//                    System.out.println("Gauge:" + k + "=" + v.getValue());
//                });
//
//                SortedMap<String, Meter> meters = metrics.getMeters();
//                meters.forEach( (k,v) -> {
//                    System.out.println("meter:" + k + "=" + v.getCount());
//                });
//                SortedMap<String, Timer> timers = metrics.getTimers();
//                timers.forEach( (k,v) -> {
//                    System.out.println("timers:" + k + "=" + v.getCount());
//                });
            }
        };

        executorService.submit(tpsr);*/




        //metrics.register(MetricRegistry.name(SpringDatasourceApplication.class, "da", "size");



//        ConsoleReporter rep = ConsoleReporter.forRegistry(metrics)
//                .convertRatesTo(TimeUnit.SECONDS)
//                .convertDurationsTo(TimeUnit.MILLISECONDS)
//                .build();
//
//        rep.start(5,TimeUnit.SECONDS);





        /*System.out.println("dataSource is:" + dataSource.getClass().getName());
        for (int i = 0; i < 5; i++) {
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
                            //break;
                        }
                        //ps.setInt(1, id);
                        //ps.setString(2, "xiaomin11");
                        //Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
                        //ps.setTimestamp(3, ts);
                        //ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//                        int ct = ps.executeUpdate("insert into users values" +
//                                " (" + id + "," + "\'xiaomin11\'" + ",\'" + ts.toString() + "\',\'" + ts.toString() + "\')");
//                        if (id % 10000 == 0) {
//                            //System.out.println("count:" + id);
//                        }

                        ResultSet rs  = ps.executeQuery("select * from users limit 2,3");

                        while (rs.next()){
                            long uid = rs.getInt(1);
                            String name = rs.getString(2);
                            //System.out.println("uid:" + uid + "  name:" + name);
                        }
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
            Thread.sleep(20000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        run = false;
        System.out.println("start down...:");
        executorService.shutdown();
        System.out.println("total count:" + count.get());*/
    }


    /*@ConditionalOnProperty("dhcp")
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


    @ConditionalOnMissingBean(DataSource.class)
    @Bean()
    public HikariDataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://10.10.51.212:13306/test?characterEncoding=utf8&useSSL=false");
        //props.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        props.setProperty("username", "root");
        props.setProperty("password", "password");
        //props.setProperty("databaseName", "dbtest");
        //props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        config.setMaximumPoolSize(12);
        config.setConnectionTimeout(17000);
        config.setPoolName("poll-online");
        config.setMetricsTrackerFactory(new InfluxDBMetricsTrackerFactory());
        //config.setHealthCheckRegistry(healthCheckRegistry);
        HikariDataSource ds = new HikariDataSource(config);

        return ds;
    }*/
}
