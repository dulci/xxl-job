package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.ScheduleService;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(
		version = "${xxl.job.overseer.service.version}",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"
)
@Slf4j
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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve schedule report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(persent);
		this.log.info(stringBuffer.toString());
		return null;
	}
}
