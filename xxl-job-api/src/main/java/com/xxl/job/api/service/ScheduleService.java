package com.xxl.job.api.service;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface ScheduleService {
	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param percent        百分比
	 * @return 0：继续执行，1：中止
	 */
	Integer report(Integer taskInstanceId, String ip, Double percent);
}
