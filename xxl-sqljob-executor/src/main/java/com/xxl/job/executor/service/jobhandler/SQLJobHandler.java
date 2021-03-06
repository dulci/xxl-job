package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.thread.TriggerCallbackThread;
import com.xxl.job.core.util.LogInfoUtil;
import com.xxl.job.executor.core.model.xxljob.XxlJobInfo;
import com.xxl.job.executor.core.model.xxljob.XxlJobLog;
import com.xxl.job.executor.dao.crawlerself.CrawlerselfExecutorSQLDao;
import com.xxl.job.executor.dao.gcxx.GcxxExecutorSQLDao;
import com.xxl.job.executor.dao.pc.PcExecutorSQLDao;
import com.xxl.job.executor.dao.waterdrop.WaterdropExecutorSQLDao;
import com.xxl.job.executor.dao.xxljob.XxlJobInfoDao;
import com.xxl.job.executor.dao.xxljob.XxlJobLogDao;
import com.xxl.job.executor.dao.zhyx.ZhyxExecutorSQLDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JobHandler(value = "sqlJobHandler")
@Component
public class SQLJobHandler extends IJobHandler {
	@Autowired
	private XxlJobInfoDao xxlJobInfoDao;
	@Autowired
	private XxlJobLogDao xxlJobLogDao;
	@Autowired
	private GcxxExecutorSQLDao gcxxExecutorSQLDao;
	@Autowired
	private WaterdropExecutorSQLDao waterdropExecutorSQLDao;
	@Autowired
	private CrawlerselfExecutorSQLDao crawlerselfExecutorSQLDao;
	@Autowired
	private ZhyxExecutorSQLDao zhyxExecutorSQLDao;
	@Autowired
	private PcExecutorSQLDao pcExecutorSQLDao;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		new SendMessageThread(param).start();
		return PROCESS;
	}

	private class SendMessageThread extends Thread {

		private String param;

		public SendMessageThread(String param) {
			this.param = param;
		}

		@Override
		public void run() {
			long jobBegin = System.currentTimeMillis();
			XxlJobLogger.log("sql execution begin");
			TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), new ReturnT(ReturnT.PROCESSING.getCode(), "jobId is empty")));

			// 1 获得任务
			Integer jobId = LogInfoUtil.getJobId();
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
			XxlJobLog xxlJobLog = xxlJobLogDao.load(LogInfoUtil.getLogId());
			if (xxlJobLog == null) {
				XxlJobLogger.log("log dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "log dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}
			xxlJobLog.setHandleCode(100);
			xxlJobLog.setHandleTime(new Date());
			xxlJobLogDao.updateHandleInfo(xxlJobLog);

			// 2 获得数据源
			String datasource = xxlJobInfo.getDatasource();
			if (StringUtils.isEmpty(datasource)) {
				XxlJobLogger.log("datasource dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "datasource dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			if (!"waterdrop".equals(datasource) && !"gcxx".equals(datasource) && !"crawlerself".equals(datasource) && !"pc".equals(datasource) && !"zhyx".equals(datasource)) {
				XxlJobLogger.log("unrecognized datasource");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "unrecognized datasource");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}

			// 3 获得执行SQL
			String executorSQL = xxlJobInfo.getExecutorSQL().replace("\r\n", " ");
			if (StringUtils.isEmpty(executorSQL)) {
				XxlJobLogger.log("executorSQL dosen't exist");
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "executorSQL dosen't exist");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
				return;
			}
			List<String> sqls = new ArrayList<>();
			if (executorSQL.contains(";")) {
				sqls.addAll(Arrays.asList(executorSQL.split(";")));
			} else {
				sqls.add(executorSQL);
			}

			// 4 执行SQL
			for (String sql : sqls) {
				if (StringUtils.isEmpty(sql)) {
					continue;
				}
				XxlJobLogger.log("current execution sql is:\r\n\r\n" + sql + "\r\n");
				long begin = System.currentTimeMillis();
				if ("waterdrop".equals(datasource)) {
					try {
						waterdropExecutorSQLDao.executorSQL(sql);
					} catch (Exception e) {
						XxlJobLogger.log(e);
						ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
						TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
						return;
					}
				} else if ("crawlerself".equals(datasource)) {
					try {
						crawlerselfExecutorSQLDao.executorSQL(sql);
					} catch (Exception e) {
						XxlJobLogger.log(e);
						ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
						TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
						return;
					}
				} else if ("pc".equals(datasource)) {
					try {
						pcExecutorSQLDao.executorSQL(sql);
					} catch (Exception e) {
						XxlJobLogger.log(e);
						ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
						TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
						return;
					}
				} else if ("zhyx".equals(datasource)) {
					try {
						zhyxExecutorSQLDao.executorSQL(sql);
					} catch (Exception e) {
						XxlJobLogger.log(e);
						ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
						TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
						return;
					}
				} else {
					try {
						gcxxExecutorSQLDao.executorSQL(sql);
					} catch (Exception e) {
						XxlJobLogger.log(e);
						ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "execution occur exception");
						TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
						return;
					}
				}
				long end = System.currentTimeMillis();
				XxlJobLogger.log("current execution finished in " + ((double) (end - begin)) / 1000.0 + "s");
			}


			// 5  状态回转
			long jobEnd = System.currentTimeMillis();
			XxlJobLogger.log("total execution is: " + ((double) (jobEnd - jobBegin)) / 1000.0 + "s");
			ReturnT result = new ReturnT(ReturnT.SUCCESS.getCode(), "execute success");
			TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));

			return;
		}
	}
}
