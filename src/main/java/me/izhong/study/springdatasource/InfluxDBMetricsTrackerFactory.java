package me.izhong.study.springdatasource;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

public class InfluxDBMetricsTrackerFactory implements MetricsTrackerFactory {

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new InfluxDBMetricsTracker(poolName,poolStats);
    }

}
