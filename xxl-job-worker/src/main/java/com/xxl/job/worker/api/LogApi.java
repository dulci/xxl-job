package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service
@Slf4j
public class LogApi {
	@Autowired
	private LogService logService;

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param log            日志信息
	 * @return 0：成功
	 */
	public Integer report(String taskInstanceId, String log) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			this.log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (StringUtils.isEmpty(log)) {
			this.log.error("log is empty");
			return -1;
		}
		String ip = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			return logService.report(taskInstanceId, ip, log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
