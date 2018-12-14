package com.xxl.job.api.service;

import com.xxl.job.api.enums.JobStatus;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface StatusService {
	/**
	 * Status Report 状态汇报
	 *
	 * @param taskId 任务ID
	 * @param status 状态值
	 */
	void report(String taskId, Integer status);

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskId 任务ID
	 * @param jobStatus 状态值
	 */
	void report(String taskId, JobStatus jobStatus);
}
