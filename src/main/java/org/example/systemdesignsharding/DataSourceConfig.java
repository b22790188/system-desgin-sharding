package org.example.systemdesignsharding;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Log4j2
public class DataSourceConfig {


    @Bean
    public DataSource shard1(@Value("${shard1.url}") String url
        , @Value("${shard.username}") String username
        , @Value("${shard.password}") String password
        , @Value("${shard.driver-class-name}") String driverClassName) {

        return DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName(driverClassName)
            .build();
    }

    @Bean
    public DataSource shard2(@Value("${shard2.url}") String url
        , @Value("${shard.username}") String username
        , @Value("${shard.password}") String password
        , @Value("${shard.driver-class-name}") String driverClassName) {

        return DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName(driverClassName)
            .build();
    }

    @Bean
    public DataSource shard3(@Value("${shard3.url}") String url
        , @Value("${shard.username}") String username
        , @Value("${shard.password}") String password
        , @Value("${shard.driver-class-name}") String driverClassName) {

        return DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName(driverClassName)
            .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate1(@Qualifier("shard1") DataSource dataSource1) {
        return new NamedParameterJdbcTemplate(dataSource1);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate2(@Qualifier("shard2") DataSource dataSource2) {
        return new NamedParameterJdbcTemplate(dataSource2);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate3(@Qualifier("shard3") DataSource dataSource3) {
        return new NamedParameterJdbcTemplate(dataSource3);
    }

}
