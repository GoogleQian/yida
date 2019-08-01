package com.etar.purifier.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * Created with Intellij IDEA.
 * @author mgq
 * @version 2019/5/5
 */
@Configuration
@EnableConfigurationProperties(ShardingMasterSlaveConfig.class)
@ConditionalOnProperty({"sharding.jdbc.data-sources.ds_master.jdbcUrl", "sharding.jdbc.master-slave-rule.master-data-source-name"})
public class ShardingDataSourceConfig {
    private static Logger log = LoggerFactory.getLogger(ShardingDataSourceConfig.class);
    @Autowired(required = false)
    private ShardingMasterSlaveConfig shardingMasterSlaveConfig;

    @Bean("dataSource")
    public DataSource masterSlaveDataSource() throws SQLException {
        shardingMasterSlaveConfig.getDataSources().forEach((k, v) -> configDataSourceHikari(v));
        Map<String, DataSource> dataSourceMap = Maps.newHashMap();
        dataSourceMap.putAll(shardingMasterSlaveConfig.getDataSources());
        //创建读写分离的JDBC驱动
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, shardingMasterSlaveConfig.getMasterSlaveRule(), Maps.newHashMap(),new Properties());
        log.info("masterSlaveDataSource config complete");
        return dataSource;
    }

	/**
	 * 德鲁伊数据源配置
     * @param druidDataSource 德鲁伊数据源
     */
    private void configDataSourceDruid(DruidDataSource druidDataSource) {
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(1);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
        druidDataSource.setUseGlobalDataSourceStat(true);
        try {
            druidDataSource.setFilters("stat,wall,slf4j");
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
    }
    /**
     * hikari数据源配置
     * @param hikariDataSource hikari数据源
     */
    private void configDataSourceHikari(HikariDataSource hikariDataSource) {
        hikariDataSource.setMinimumIdle(0);
        hikariDataSource.setMaximumPoolSize(15);
        hikariDataSource.setAutoCommit(true);
        hikariDataSource.setIdleTimeout(60000);
        hikariDataSource.setPoolName("DatebookHikariCP");
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setConnectionTestQuery("select 'x'");
        hikariDataSource.setConnectionTimeout(30000);
    }
}
