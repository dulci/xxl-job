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
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, Integer status) {
		return 0;
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, JobStatus jobStatus) {
		return 0;
	}
}
