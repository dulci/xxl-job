package com.xxl.job.executor.dao;

import com.xxl.job.executor.core.model.XxlJobSubLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by dul-c on 2018-12-17.
 */
@Mapper
public interface XxlJobSubLogDao {

	public int save(XxlJobSubLog xxlJobLog);
}
