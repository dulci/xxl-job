package com.xxl.job.api.service;

import com.xxl.job.api.enums.JobStatus;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface StatusService {
	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @return 0：成功
	 */
	Integer report(Integer taskInstanceId, Integer status);

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @return 0：成功
	 */
	Integer report(Integer taskInstanceId, JobStatus jobStatus);
}
