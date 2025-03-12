package com.ohgiraffers.mybatisspring.config;

import com.ohgiraffers.mybatisspring.section01.factorybean.MenuMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* 설명. 나중에는 간편히 할 수 있으나 이번 프로젝트에서는 추가적인 설정을 할 예정 */
@Configuration
public class MybatisConfiguration {
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        /* 설명. 데이터베이스 연결 시도 유지 시간 */
        dataSource.setConnectionTimeout(30000); // 30초

        /* 설명. 데이터베이스 유휴 시간(커넥트되고 이후 쿼리가 날아가지 않아도 대기하는 시간) */
        dataSource.setIdleTimeout(600000);      // 10분

        /* 설명. 데이터베이스 연결 최대 유지 시간 */
        dataSource.setMaxLifetime(1800000);     // 30분

        dataSource.setMaximumPoolSize(50);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.addMapper(MenuMapper.class);

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());        // datasource 등록
        factoryBean.setConfiguration(configuration);    // mapper 등록

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }
}
