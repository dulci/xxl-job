package com.xxl.job.api.service;

import com.xxl.job.api.dto.SubJobInfoForBatchCreate;

import java.util.List;

/**
 * Created by dul-c on 2018-12-12.
 */
public interface SubJobService {
	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param ip                 ip
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	Integer create(Integer mainTaskInstanceId, String ip, Integer index);

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param ip                 ip
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	Integer create(Integer mainTaskInstanceId, String ip, Integer index, Integer total);

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param ip                 ip
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	List<SubJobInfoForBatchCreate> batchCreate(Integer mainTaskInstanceId, String ip, Integer total);

	/**
	 * SubJob isContinueProcess 是否执行询问
	 *
	 * @param taskInstanceId 任务实例ID
	 * @return 0：执行，1：中止
	 */
	Integer isContinueProcess(Integer taskInstanceId);
}
