package com.xxl.job.api.service;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface ScheduleService {
	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskId  任务ID
	 * @param persent 百分比
	 * @return 0：继续执行，1：中止
	 */
	public Integer report(String taskId, Double persent);
}
