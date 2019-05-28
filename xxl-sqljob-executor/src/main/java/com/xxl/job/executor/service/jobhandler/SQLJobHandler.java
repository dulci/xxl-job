package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.thread.TriggerCallbackThread;
import com.xxl.job.core.util.LogInfoUtil;
import com.xxl.job.executor.core.model.xxljob.XxlJobInfo;
import com.xxl.job.executor.dao.gcxx.GcxxExecutorSQLDao;
import com.xxl.job.executor.dao.waterdrop.WaterdropExecutorSQLDao;
import com.xxl.job.executor.dao.xxljob.XxlJobInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
	@Autowired
	private GcxxExecutorSQLDao gcxxExecutorSQLDao;
	@Autowired
	private WaterdropExecutorSQLDao waterdropExecutorSQLDao;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		new SendMessageThread(param).start();
		return NO_START;
	}

	private class SendMessageThread extends Thread {

		private String param;

		public SendMessageThread(String param) {
			this.param = param;
		}

		@Override
		public void run() {
			XxlJobLogger.log("jobId is empty");
			TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), new ReturnT(ReturnT.PROCESSING.getCode(), "jobId is empty")));

			// 1 获得任务
			Integer jobId = LogInfoUtil.getLogId();
			if (jobId == null) {
				XxlJobLogger.log("jobId is empty");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "jobId is empty");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}
			XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
			if (xxlJobInfo == null) {
				XxlJobLogger.log("job dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "job dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			// 2 获得数据源
			String datasource = xxlJobInfo.getDatasource();
			if (StringUtils.isEmpty(datasource)) {
				XxlJobLogger.log("datasource dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "datasource dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			if (!"waterdrop".equals(datasource) && !"gcxx".equals(datasource)) {
				XxlJobLogger.log("unrecognized datasource");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "unrecognized datasource");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			// 3 获得执行SQL
			String executorSQL = xxlJobInfo.getExecutorSQL();
			if (StringUtils.isEmpty(executorSQL)) {
				XxlJobLogger.log("executorSQL dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "executorSQL dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			// 4 执行SQL
			XxlJobLogger.log("the execute sql is:");
			XxlJobLogger.log(executorSQL);
			long begin = System.currentTimeMillis();
			if ("waterdrop".equals(datasource)) {
				try {
					waterdropExecutorSQLDao.executorSQL(executorSQL);
				} catch (Exception e) {
					XxlJobLogger.log(e);
					ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
					TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
					return;
				}
			} else {
				try {
					gcxxExecutorSQLDao.executorSQL(executorSQL);
				} catch (Exception e) {
					XxlJobLogger.log(e);
					ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
					TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
					return;
				}
			}

			// 5  状态回转
			long end = System.currentTimeMillis();
			XxlJobLogger.log("sql execution is:" + ((double) (((int) (end - begin)) / 1000)) / 1000.0);
			ReturnT result = new ReturnT(ReturnT.SUCCESS.getCode(), "execute success");
			TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));

			return;
		}
	}
}
