package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.ScheduleService;
import com.xxl.job.worker.util.IpUtil;
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
	 * @param percent        百分比
	 * @return 0：继续执行，1：中止
	 */
	public Integer report(Integer taskInstanceId, Double percent) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (percent == null) {
			log.error("percent can not be empty");
			return -1;
		}
		if (percent < 0.0 || percent > 100.0) {
			log.error("percent must be bwteen 0.0 and 100.0");
			return -1;
		}
		Integer status = -1;
		try {
			status = scheduleService.report(taskInstanceId, IpUtil.getIp(), percent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
