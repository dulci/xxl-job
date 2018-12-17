package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.service.SubJobService;
import com.xxl.job.executor.dao.XxlJobLogDao;
import com.xxl.job.executor.dao.XxlJobSubLogDao;
import org.springframework.beans.factory.annotation.Autowired;

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
	@Autowired
	private XxlJobLogDao xxlJobLogDao;
	@Autowired
	private XxlJobSubLogDao xxlJobSubLogDao;

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	@Override
	public Integer create(Integer mainTaskInstanceId, Integer index) {
		xxlJobLogDao.load(mainTaskInstanceId);
		return null;
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	@Override
	public Integer create(Integer mainTaskInstanceId, Integer index, Integer total) {
		return null;
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	@Override
	public List<SubJobInfoForBatchCreate> batchCreate(Integer mainTaskInstanceId, Integer total) {
		return null;
	}

	/**
	 * SubJob isContinueProcess 是否执行询问
	 *
	 * @param taskInstanceId 任务实例ID
	 * @return 0：执行，1：中止
	 */
	@Override
	public Integer isContinueProcess(Integer taskInstanceId) {
		return null;
	}
}
