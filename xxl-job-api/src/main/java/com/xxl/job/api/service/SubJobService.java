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
	 * @param mainTaskId 主任务ID
	 * @param index      子任务序号
	 * @return 子任务ID
	 */
	String create(String mainTaskId, Integer index);

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskId 主任务ID
	 * @param index      子任务序号
	 * @param total      子任务序总数
	 * @return 子任务ID
	 */
	String create(String mainTaskId, Integer index, Integer total);

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskId 主任务ID
	 * @param total      子任务序总数
	 * @return 子任务ID
	 */
	List<SubJobInfoForBatchCreate> batchCreate(String mainTaskId, Integer total);

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param taskId 任务ID
	 * @return 0：执行，1：中止
	 */
	Integer isContinueProcess(String taskId);
}
