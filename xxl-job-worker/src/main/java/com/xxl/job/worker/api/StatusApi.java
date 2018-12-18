package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.api.service.StatusService;
import com.xxl.job.worker.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service
@Slf4j
public class StatusApi {
	@Autowired
	private StatusService statusService;

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	public Integer report(Integer taskInstanceId, Integer status, String msg) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (status == null) {
			log.error("status can not be empty");
			return -1;
		}
		try {
			return statusService.report(taskInstanceId, IpUtil.getIp(), status, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	public Integer report(Integer taskInstanceId, JobStatus jobStatus, String msg) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			log.error("taskInstanceId can not be empty");
			return -1;
		}
		if (jobStatus == null) {
			log.error("jobStatus can not be empty");
			return -1;
		}
		try {
			return statusService.report(taskInstanceId, IpUtil.getIp(), jobStatus, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
