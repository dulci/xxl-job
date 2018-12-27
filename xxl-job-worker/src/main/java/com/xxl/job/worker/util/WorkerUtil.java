package com.xxl.job.worker.util;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.worker.api.LogApi;
import com.xxl.job.worker.api.ScheduleApi;
import com.xxl.job.worker.api.StatusApi;
import com.xxl.job.worker.api.SubJobApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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

	private static Logger logger = LoggerFactory.getLogger(WorkerUtil.class);

	public BoundWorkerUtil boundWorkerUtil(Integer taskInstanceId) {
		return BoundWorkerUtil.getInstance(taskInstanceId, this);
	}

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param log            日志信息
	 * @return 0：成功
	 */
	public Integer reportLog(Integer taskInstanceId, String log) {
		logger.info(log);
		return logApi.report(taskInstanceId, log);
	}

	/**
	 * Log Report 日志汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param e              异常信息
	 * @return 0：成功
	 */
	public Integer reportException(Integer taskInstanceId, Exception e) {
		e.printStackTrace();
		return logApi.report(taskInstanceId, e);
	}

	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param percent        百分比
	 * @return 0：继续执行，1：中止
	 */
	public Integer reportPercent(Integer taskInstanceId, Double percent) {
		return scheduleApi.report(taskInstanceId, percent);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @return 0：成功
	 */
	public Integer reportStatus(Integer taskInstanceId, Integer status) {
		return statusApi.report(taskInstanceId, status, null);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param status         状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	public Integer reportStatus(Integer taskInstanceId, Integer status, String msg) {
		return statusApi.report(taskInstanceId, status, msg);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @return 0：成功
	 */
	public Integer reportStatus(Integer taskInstanceId, JobStatus jobStatus) {
		return statusApi.report(taskInstanceId, jobStatus, null);
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param jobStatus      状态值
	 * @param msg            备注消息
	 * @return 0：成功
	 */
	public Integer reportStatus(Integer taskInstanceId, JobStatus jobStatus, String msg) {
		return statusApi.report(taskInstanceId, jobStatus, msg);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	public Integer createSubJob(Integer mainTaskInstanceId, Integer index) {
		return subJobApi.create(mainTaskInstanceId, index);
	}

	/**
	 * SubJob createFinish 创建子任务完成
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @return 0：成功
	 */
	public Integer createFinish(Integer mainTaskInstanceId) {
		return subJobApi.createFinish(mainTaskInstanceId);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	public Integer createSubJob(Integer mainTaskInstanceId, Integer index, Integer total) {
		return subJobApi.create(mainTaskInstanceId, index, total);
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	public List<SubJobInfoForBatchCreate> batchCreateSubJob(Integer mainTaskInstanceId, Integer total) {
		return subJobApi.batchCreate(mainTaskInstanceId, total);
	}

	/**
	 * SubJob isContinueProcess 是否执行询问
	 *
	 * @param taskInstanceId 任务实例ID
	 * @return 0：执行，1：中止
	 */
	public Integer isContinueProcess(Integer taskInstanceId) {
		return subJobApi.isContinueProcess(taskInstanceId);
	}
}
