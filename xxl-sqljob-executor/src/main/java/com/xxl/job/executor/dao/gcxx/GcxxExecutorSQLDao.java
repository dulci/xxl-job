package com.xxl.job.executor.dao.gcxx;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GcxxExecutorSQLDao {
	public void executorSQL(@Param("sql") String sql);
}
