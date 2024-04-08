package org.qiyu.live.framework.datasource.starter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author: QingY
 * @Date: Created in 15:02 2024-04-08
 * @Description:
 */
@Configuration
public class ShardingJdbcDataSourceAutoInitConnectionConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDataSourceAutoInitConnectionConfig.class);

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info(" ==================  [ShardingJdbcDatasourceAutoInitConnectionConfig] dataSource: {}", dataSource);
            // 手动触发连接池的连接创建
            Connection connection = dataSource.getConnection();
        };
    }

}
