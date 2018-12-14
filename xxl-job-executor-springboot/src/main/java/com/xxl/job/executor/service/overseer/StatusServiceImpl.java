package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.api.service.StatusService;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(
		version = "${xxl.job.overseer.service.version}",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"
)
public class StatusServiceImpl implements StatusService {

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskId 任务ID
	 * @param status 状态值
	 */
	@Override
	public void report(String taskId, Integer status) {

	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskId    任务ID
	 * @param jobStatus 状态值
	 */
	@Override
	public void report(String taskId, JobStatus jobStatus) {

	}
}
