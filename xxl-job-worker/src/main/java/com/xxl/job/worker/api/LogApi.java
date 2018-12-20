package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.LogService;
import com.xxl.job.worker.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service
@Slf4j
public class LogApi {
	@Autowired
	private LogService logService;

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param log            日志信息
	 * @return 0：成功
	 */
	public Integer report(Integer taskInstanceId, String log) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			this.log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (StringUtils.isEmpty(log)) {
			this.log.error("log is empty");
			return -1;
		}
		try {
			return logService.report(taskInstanceId, IpUtil.getIp(), log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param e              异常信息
	 * @return 0：成功
	 */
	public Integer report(Integer taskInstanceId, Exception e) {
		if (e == null) {
			this.log.error("exception is null");
			return -1;
		}
		return logService.report(taskInstanceId, IpUtil.getIp(), e);
	}
}
