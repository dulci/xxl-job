package com.xxl.job.executor.core.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.xxl.job.executor.dao.gcxx", sqlSessionTemplateRef = "gcxxSqlSessionTemplate")
public class GcxxDataSourceConfig {
	@Bean(name = "gcxxDataSource")
	@ConfigurationProperties(prefix = "gcxx.datasource")
	public DataSource gcxxDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "gcxxSqlSessionFactory")
	public SqlSessionFactory gcxxSqlSessionFactory(@Qualifier("gcxxDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/gcxx/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "gcxxTransactionManager")
	public DataSourceTransactionManager gcxxTransactionManager(@Qualifier("gcxxDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "gcxxSqlSessionTemplate")
	public SqlSessionTemplate gcxxSqlSessionTemplate(@Qualifier("gcxxSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
