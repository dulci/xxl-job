package com.xxl.job.executor.dao.waterdrop;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WaterdropExecutorSQLDao {
	public void executorSQL(String sql);
}
