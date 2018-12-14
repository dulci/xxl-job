package com.xxl.job.api.service;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface LogService {
	/**
	 * Status Report 状态汇报
	 *
	 * @param taskId 任务ID
	 * @param ip     ip
	 * @param log    日志信息
	 */
	void report(String taskId, String ip, String log);
}
