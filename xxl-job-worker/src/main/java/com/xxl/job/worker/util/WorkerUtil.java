package com.xxl.job.worker.util;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.worker.api.LogApi;
import com.xxl.job.worker.api.ScheduleApi;
import com.xxl.job.worker.api.StatusApi;
import com.xxl.job.worker.api.SubJobApi;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by dul-c on 2018-12-14.
 */
@Service
public class WorkerUtil {
	@Autowired
	private LogApi logApi;
	@Autowired
	private ScheduleApi scheduleApi;
	@Autowired
	private StatusApi statusApi;
	@Autowired
	private SubJobApi subJobApi;

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param log            日志信息
	 * @return 0：成功
	 */
	public Integer report(String taskInstanceId, String log) {
		return logApi.report(taskInstanceId, log);
	}

	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param persent        百分比
	 * @return 0：继续执行，1：中止
	 */
	public Integer report(String taskInstanceId, Double persent) {
		return scheduleApi.report(taskInstanceId, persent);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @return 0：成功
	 */
	public Integer report(String taskInstanceId, Integer status) {
		return statusApi.report(taskInstanceId, status);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @return 0：成功
	 */
	public Integer report(String taskInstanceId, JobStatus jobStatus) {
		return statusApi.report(taskInstanceId, jobStatus);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	public String create(String mainTaskInstanceId, Integer index) {
		return subJobApi.create(mainTaskInstanceId, index);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	public String create(String mainTaskInstanceId, Integer index, Integer total) {
		return subJobApi.create(mainTaskInstanceId, index, total);
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	public List<SubJobInfoForBatchCreate> batchCreate(String mainTaskInstanceId, Integer total) {
		return subJobApi.batchCreate(mainTaskInstanceId, total);
	}

	/**
	 * SubJob isContinueProcess 是否执行询问
	 *
	 * @param taskInstanceId 任务实例ID
	 * @return 0：执行，1：中止
	 */
	public Integer isContinueProcess(String taskInstanceId) {
		return subJobApi.isContinueProcess(taskInstanceId);
	}
}
