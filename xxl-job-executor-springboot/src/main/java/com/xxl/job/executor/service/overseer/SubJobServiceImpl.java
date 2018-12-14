package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.service.SubJobService;

import java.util.List;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(
		version = "${xxl.job.overseer.service.version}",
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}"
)
public class SubJobServiceImpl implements SubJobService {

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskId 主任务ID
	 * @param index      子任务序号
	 * @return 子任务ID
	 */
	@Override
	public String create(String mainTaskId, Integer index) {
		return null;
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskId 主任务ID
	 * @param index      子任务序号
	 * @param total      子任务序总数
	 * @return 子任务ID
	 */
	@Override
	public String create(String mainTaskId, Integer index, Integer total) {
		return null;
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskId 主任务ID
	 * @param total      子任务序总数
	 * @return 子任务ID
	 */
	@Override
	public List<SubJobInfoForBatchCreate> batchCreate(String mainTaskId, Integer total) {
		return null;
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param taskId 任务ID
	 * @return 0：执行，1：中止
	 */
	@Override
	public Integer isContinueProcess(String taskId) {
		return null;
	}
}
