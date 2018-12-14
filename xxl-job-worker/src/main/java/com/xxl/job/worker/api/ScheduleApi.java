package com.xxl.job.worker.api;

import com.xxl.job.api.service.ScheduleService;

/**
 * Created by dul-c on 2018-12-12.
 */
public class ScheduleApi {
	private ScheduleService scheduleService;

	public Integer report(String taskId, Double persent) {
		return scheduleService.report(taskId, persent);
	}
}
