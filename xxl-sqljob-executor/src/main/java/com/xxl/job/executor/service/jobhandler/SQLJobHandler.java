package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.LogInfoUtil;
import com.xxl.job.executor.core.model.xxljob.XxlJobInfo;
import com.xxl.job.executor.dao.xxljob.XxlJobInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 跨平台RabbitMQ任务
 *
 * @author zhangjl-h
 */
@JobHandler(value = "sqlJobHandler")
@Component
public class SQLJobHandler extends IJobHandler {
	@Autowired
	private XxlJobInfoDao xxlJobInfoDao;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		// 1 获得任务
		Integer jobId = LogInfoUtil.getLogId();
		if (jobId == null) {
			XxlJobLogger.log("jobId is empty");
			return FAIL;
		}
		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
		if (xxlJobInfo == null) {
			XxlJobLogger.log("job dosen't exist");
			return FAIL;
		}

		// 2 获得数据源
		String datasource = xxlJobInfo.getDatasource();
		if (StringUtils.isEmpty(datasource)) {
			XxlJobLogger.log("datasource dosen't exist");
			return FAIL;
		}

		if (!"waterdrop".equals(datasource) && !"gcxx".equals(datasource)) {
			XxlJobLogger.log("unrecognized datasource");
			return FAIL;
		}

		// 3 获得执行SQL
		String executorSQL = xxlJobInfo.getExecutorSQL();
		if (StringUtils.isEmpty(executorSQL)) {
			XxlJobLogger.log("executorSQL dosen't exist");
			return FAIL;
		}

		return SUCCESS;
	}
}
