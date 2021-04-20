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
@MapperScan(basePackages = "com.xxl.job.executor.dao.zhyx", sqlSessionTemplateRef = "zhyxSqlSessionTemplate")
public class ZhyxDataSourceConfig {
	@Bean(name = "zhyxDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.zhyx")
	public DataSourceProperties zhyxDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "zhyxDataSource")
	public DataSource zhyxDataSource(@Qualifier("zhyxDataSourceProperties") DataSourceProperties zhyxDataSourceProperties) {
		return zhyxDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "zhyxSqlSessionFactory")
	public SqlSessionFactory zhyxSqlSessionFactory(@Qualifier("zhyxDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/zhyx/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "zhyxTransactionManager")
	public DataSourceTransactionManager zhyxTransactionManager(@Qualifier("zhyxDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "zhyxSqlSessionTemplate")
	public SqlSessionTemplate zhyxSqlSessionTemplate(@Qualifier("zhyxSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
