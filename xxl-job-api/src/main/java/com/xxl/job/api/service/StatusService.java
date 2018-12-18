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
	 * @param ip             ip
	 * @param status         状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	Integer report(Integer taskInstanceId, String ip, Integer status, String msg);

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param jobStatus      状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	Integer report(Integer taskInstanceId, String ip, JobStatus jobStatus, String msg);
}
