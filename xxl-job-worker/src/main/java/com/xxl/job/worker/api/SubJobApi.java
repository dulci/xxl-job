package com.xxl.job.worker.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.service.SubJobService;
import com.xxl.job.worker.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service
@Slf4j
public class SubJobApi {
	@Autowired
	private SubJobService subJobService;

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	public Integer create(Integer mainTaskInstanceId, Integer index) {
		if (StringUtils.isEmpty(mainTaskInstanceId)) {
			log.error("mainTaskInstanceId can not be empty");
			return 0;
		}
		if (index == null) {
			log.error("index can not be empty");
			return 0;
		}
		try {
			return subJobService.create(mainTaskInstanceId, IpUtil.getIp(), index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * SubJob createFinish 创建子任务完成
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @return 0：成功
	 */
	public Integer createFinish(Integer mainTaskInstanceId) {
		if (StringUtils.isEmpty(mainTaskInstanceId)) {
			log.error("mainTaskInstanceId can not be empty");
			return 0;
		}
		return subJobService.createFinish(mainTaskInstanceId);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	public Integer create(Integer mainTaskInstanceId, Integer index, Integer total) {
		if (StringUtils.isEmpty(mainTaskInstanceId)) {
			log.error("mainTaskInstanceId can not be empty");
			return 0;
		}
		if (index == null) {
			log.error("index can not be empty");
			return 0;
		}
		try {
			return subJobService.create(mainTaskInstanceId, IpUtil.getIp(), index, total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	public List<SubJobInfoForBatchCreate> batchCreate(Integer mainTaskInstanceId, Integer total) {
		if (StringUtils.isEmpty(mainTaskInstanceId)) {
			log.error("mainTaskInstanceId can not be empty");
			return new ArrayList<>();
		}
		if (total == null) {
			log.error("total can not be empty");
			return new ArrayList<>();
		}
		try {
			return subJobService.batchCreate(mainTaskInstanceId, IpUtil.getIp(), total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * SubJob isContinueProcess 是否执行询问
	 *
	 * @param taskInstanceId 任务实例ID
	 * @return 0：执行，1：中止
	 */
	public Integer isContinueProcess(Integer taskInstanceId) {
		if (StringUtils.isEmpty(taskInstanceId)) {
			log.error("taskInstanceId can not be empty");
			return -1;
		}
		try {
			return subJobService.isContinueProcess(taskInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
