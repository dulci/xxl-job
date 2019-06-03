package com.xxl.job.executor.dao.crawlerself;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CrawlerselfExecutorSQLDao {
	public void executorSQL(@Param("sql") String sql);
}
