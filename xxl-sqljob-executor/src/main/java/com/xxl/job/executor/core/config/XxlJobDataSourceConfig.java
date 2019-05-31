package com.xxl.job.executor.core.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.xxl.job.executor.dao.xxljob", sqlSessionTemplateRef = "xxljobSqlSessionTemplate")
public class XxlJobDataSourceConfig {
	@Primary
	@Bean(name = "xxljobDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.xxljob")
	public DataSourceProperties xxljobDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "xxljobDataSource")
	public DataSource xxljobDataSource(DataSourceProperties xxljobDataSourceProperties) {
		return xxljobDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "xxljobSqlSessionFactory")
	public SqlSessionFactory xxljobSqlSessionFactory(@Qualifier("xxljobDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/xxljob/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "xxljobTransactionManager")
	public DataSourceTransactionManager xxljobTransactionManager(@Qualifier("xxljobDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "xxljobSqlSessionTemplate")
	public SqlSessionTemplate xxljobSqlSessionTemplate(@Qualifier("xxljobSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
