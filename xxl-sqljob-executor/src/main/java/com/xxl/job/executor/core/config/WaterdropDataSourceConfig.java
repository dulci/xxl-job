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
@MapperScan(basePackages = "com.xxl.job.executor.dao.waterdrop", sqlSessionTemplateRef = "waterdropSqlSessionTemplate")
public class WaterdropDataSourceConfig {
	@Bean(name = "waterdropDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.waterdrop")
	public DataSourceProperties waterdropDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "waterdropDataSource")
	public DataSource waterdropDataSource(@Qualifier("waterdropDataSourceProperties") DataSourceProperties waterdropDataSourceProperties) {
		return waterdropDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "waterdropSqlSessionFactory")
	public SqlSessionFactory waterdropSqlSessionFactory(@Qualifier("waterdropDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/waterdrop/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "waterdropTransactionManager")
	public DataSourceTransactionManager waterdropTransactionManager(@Qualifier("waterdropDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "waterdropSqlSessionTemplate")
	public SqlSessionTemplate waterdropSqlSessionTemplate(@Qualifier("waterdropSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
