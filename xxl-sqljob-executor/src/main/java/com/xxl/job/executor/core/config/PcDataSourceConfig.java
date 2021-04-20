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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.xxl.job.executor.dao.pc", sqlSessionTemplateRef = "pcSqlSessionTemplate")
public class PcDataSourceConfig {
	@Bean(name = "pcDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.pc")
	public DataSourceProperties pcDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "pcDataSource")
	public DataSource pcDataSource(@Qualifier("pcDataSourceProperties") DataSourceProperties pcDataSourceProperties) {
		return pcDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "pcSqlSessionFactory")
	public SqlSessionFactory pcSqlSessionFactory(@Qualifier("pcDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/pc/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "pcTransactionManager")
	public DataSourceTransactionManager pcTransactionManager(@Qualifier("pcDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "pcSqlSessionTemplate")
	public SqlSessionTemplate pcSqlSessionTemplate(@Qualifier("pcSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
