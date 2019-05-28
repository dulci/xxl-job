package com.xxl.job.executor.dao.waterdrop;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WaterdropExecutorSQLDao {
	public void executorSQL(@Param("sql") String sql);
}
