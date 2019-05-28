package com.xxl.job.executor.dao.gcxx;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GcxxExecutorSQLDao {
	public void executorSQL(String sql);
}
