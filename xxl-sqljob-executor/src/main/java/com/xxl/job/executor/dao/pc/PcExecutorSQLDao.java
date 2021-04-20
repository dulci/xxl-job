package com.xxl.job.executor.dao.pc;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PcExecutorSQLDao {
	public void executorSQL(@Param("sql") String sql);
}
