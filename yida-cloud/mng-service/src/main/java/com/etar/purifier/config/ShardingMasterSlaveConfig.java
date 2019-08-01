package com.etar.purifier.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with Intellij IDEA.
 * @author mgq
 * @version 2019/5/5
 */
@ConfigurationProperties(prefix = "sharding.jdbc")
public class ShardingMasterSlaveConfig {

    private Map<String, HikariDataSource> dataSources = new HashMap<>();
    private Map<String, DruidDataSource> dataSources2 = new HashMap<>();

    private MasterSlaveRuleConfiguration masterSlaveRule;

    public Map<String, HikariDataSource> getDataSources() {
        return dataSources;
    }
    public Map<String, DruidDataSource> getDataSources2() {
        return dataSources2;
    }

    public void setDataSources(Map<String, HikariDataSource> dataSources) {
        this.dataSources = dataSources;
    }
    public void setDataSources2(Map<String, DruidDataSource> dataSources2) {
        this.dataSources2 = dataSources2;
    }

    public MasterSlaveRuleConfiguration getMasterSlaveRule() {
        return masterSlaveRule;
    }

    public void setMasterSlaveRule(MasterSlaveRuleConfiguration masterSlaveRule) {
        this.masterSlaveRule = masterSlaveRule;
    }
}
