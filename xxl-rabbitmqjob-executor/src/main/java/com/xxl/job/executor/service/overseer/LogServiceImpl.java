package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.LogService;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import com.xxl.job.executor.core.model.XxlJobLog;
import com.xxl.job.executor.dao.XxlJobLogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(version = "${xxl.job.overseer.service.version}", application = "${dubbo.application.id}", protocol = "${dubbo.protocol.id}", registry = "${dubbo.registry.id}")
@Component
@Slf4j
public class LogServiceImpl implements LogService {
	@Autowired
	private XxlJobLogDao xxlJobLogDao;

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param log            日志信息
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, String log) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve log report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(log);
		this.log.info(stringBuffer.toString());
		//new LogThread(taskInstanceId, ip, log).start();
		printLog(taskInstanceId, ip, log, null);
		return 0;
	}

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param e              异常信息
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, Exception e) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve log report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(log);
		this.log.info(stringBuffer.toString());
		//new LogThread(taskInstanceId, ip, e).start();

		printLog(taskInstanceId, ip, null, e);
		return 0;
	}

	private void printLog(Integer logId, String ip, String log, Exception e) {
		XxlJobLog xxlJobLog = xxlJobLogDao.load(logId);
		if (xxlJobLog == null) {
			return;
		}
		Date triggerDate = xxlJobLog.getTriggerTime();
		if (triggerDate == null) {
			triggerDate = new Date();
		}
		String logFileName = XxlJobFileAppender.makeLogFileName(triggerDate, logId);
		XxlJobFileAppender.contextHolder.set(logFileName);
		if (StringUtils.isNotEmpty(log)) {
			XxlJobLogger.log("ip:{} , detail:{}", ip, log);
		} else if (e != null) {
			XxlJobLogger.log("ip:{} , exception detail:{}", ip, e.getClass().getName());
			XxlJobLogger.log(e);
		}
	}

	private static class LogThread extends Thread {
		private Integer logId;
		private String ip;
		private String log;
		private Exception e;

		public LogThread(Integer logId, String ip, String log) {
			this.logId = logId;
			this.ip = ip;
			this.log = log;
		}

		public LogThread(Integer logId, String ip, Exception e) {
			this.logId = logId;
			this.ip = ip;
			this.e = e;
		}

		public void run() {
			String logFileName = XxlJobFileAppender.makeLogFileName(new Date(), this.logId);
			XxlJobFileAppender.contextHolder.set(logFileName);
			if (StringUtils.isNotEmpty(log)) {
				XxlJobLogger.log("ip:{} , detail:{}", ip, log);
			} else if (e != null) {
				XxlJobLogger.log("ip:{} , exception detail:{}", ip, e.getClass().getName());
				XxlJobLogger.log(e);
			}
		}
	}
}
