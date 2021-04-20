package com.xxl.job.executor.dao.zhyx;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ZhyxExecutorSQLDao {
	public void executorSQL(@Param("sql") String sql);
}
