package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service
@Slf4j
public class ScheduleApi {
	@Autowired
	private ScheduleService scheduleService;

	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param persent        百分比
	 * @return 0：继续执行，1：中止
	 */
	public Integer report(String taskInstanceId, Double persent) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (persent == null) {
			log.error("persent can not be empty");
			return -1;
		}
		Integer status = -1;
		try {
			status = scheduleService.report(taskInstanceId, persent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
