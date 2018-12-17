package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.ScheduleService;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(
		version = "${xxl.job.overseer.service.version}",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"
)
public class ScheduleServiceImpl implements ScheduleService {

	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param persent        百分比
	 * @return 0：继续执行，1：中止
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, Double persent) {
		return null;
	}
}
