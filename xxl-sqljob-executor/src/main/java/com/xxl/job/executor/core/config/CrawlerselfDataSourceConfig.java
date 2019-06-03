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
@MapperScan(basePackages = "com.xxl.job.executor.dao.crawlerself", sqlSessionTemplateRef = "crawlerselfSqlSessionTemplate")
public class CrawlerselfDataSourceConfig {
	@Bean(name = "crawlerselfDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.crawlerself")
	public DataSourceProperties crawlerselfDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "crawlerselfDataSource")
	public DataSource crawlerselfDataSource(@Qualifier("crawlerselfDataSourceProperties") DataSourceProperties crawlerselfDataSourceProperties) {
		return crawlerselfDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "crawlerselfSqlSessionFactory")
	public SqlSessionFactory crawlerselfSqlSessionFactory(@Qualifier("crawlerselfDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/crawlerself/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "crawlerselfTransactionManager")
	public DataSourceTransactionManager crawlerselfTransactionManager(@Qualifier("crawlerselfDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "crawlerselfSqlSessionTemplate")
	public SqlSessionTemplate crawlerselfSqlSessionTemplate(@Qualifier("crawlerselfSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
