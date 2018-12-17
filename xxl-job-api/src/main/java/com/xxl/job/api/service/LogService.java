package com.xxl.job.api.service;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface LogService {
	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param log            日志信息
	 * @return 0：成功
	 */
	Integer report(Integer taskInstanceId, String ip, String log);
}
