package me.izhong.study.springdatasource;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.PoolStats;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class InfluxDBMetricsTracker implements IMetricsTracker {

    private String poolName;
    private PoolStats stats;
    private ScheduledExecutorService executorService = null;

    public InfluxDBMetricsTracker(String poolName, PoolStats stats) {
        this.poolName = poolName;
        this.stats = stats;

        executorService = Executors.newSingleThreadScheduledExecutor((r) -> {
                Thread t = new Thread(r);
                t.setName(poolName + "-COLLECT");
                return t;
        });

        executorService.scheduleAtFixedRate(() -> {
                System.out.println("getActiveConnections:" + stats.getActiveConnections());
                System.out.println("getIdleConnections:" + stats.getIdleConnections());
                System.out.println("getMaxConnections:" + stats.getMaxConnections());
        },5,1, TimeUnit.SECONDS);
    }

    public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
        //System.out.println("connectionCreatedMillis:" + connectionCreatedMillis);
     }

    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        //System.out.println("elapsedAcquiredNanos:" + elapsedAcquiredNanos);

    }

    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        //System.out.println("elapsedBorrowedMillis:" + elapsedBorrowedMillis);

    }

    public void recordConnectionTimeout() {}

    @Override
    public void close(){
        if(executorService != null)
            executorService.shutdown();
    }

}
