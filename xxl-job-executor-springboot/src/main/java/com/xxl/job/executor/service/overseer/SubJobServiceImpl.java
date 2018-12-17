package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.service.SubJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.executor.core.model.XxlJobLog;
import com.xxl.job.executor.core.model.XxlJobSubLog;
import com.xxl.job.executor.dao.XxlJobLogDao;
import com.xxl.job.executor.dao.XxlJobSubLogDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
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
	 * @param ip                 ip
	 * @param index              子任务序号
	 * @return 子任务实例ID
	 */
	@Override
	public Integer create(Integer mainTaskInstanceId, String ip, Integer index) {
		return create(mainTaskInstanceId, ip, index, null);
	}

	/**
	 * SubJob create 创建子任务
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @param ip                 ip
	 * @param index              子任务序号
	 * @param total              子任务序总数
	 * @return 子任务实例ID
	 */
	@Override
	public Integer create(Integer mainTaskInstanceId, String ip, Integer index, Integer total) {
		XxlJobLog xxlJobLog = xxlJobLogDao.load(mainTaskInstanceId);
		if (xxlJobLog == null) {
			return 0;
		}
		XxlJobSubLog xxlJobSubLog = new XxlJobSubLog();
		xxlJobSubLog.setParentId(xxlJobLog.getId());
		xxlJobSubLog.setJobGroup(xxlJobLog.getJobGroup());
		xxlJobSubLog.setJobId(xxlJobLog.getJobId());
		xxlJobSubLog.setIndex(index);
		if (total != null) {
			xxlJobSubLog.setTotal(total);
		}
		xxlJobSubLog.setExecutorParam("子任务序号：" + index);
		xxlJobSubLog.setTriggerTime(new Date());
		xxlJobSubLog.setTriggerCode(ReturnT.SUCCESS_CODE);
		xxlJobSubLog.setTriggerMsg("任务触发类型：接口触发<br>创建机器：" + ip + "<br>");
		xxlJobSubLogDao.save(xxlJobSubLog);
		return xxlJobSubLog.getId();
	}

	/**
	 * SubJob batchCreate 批量创建子任务
	 *
	 * @param mainTaskInstanceId 主任务ID
	 * @param ip                 ip
	 * @param total              子任务序总数
	 * @return 子任务实例ID列表
	 */
	@Override
	public List<SubJobInfoForBatchCreate> batchCreate(Integer mainTaskInstanceId, String ip, Integer total) {
		List<SubJobInfoForBatchCreate> result = new ArrayList<>();
		XxlJobLog xxlJobLog = xxlJobLogDao.load(mainTaskInstanceId);
		if (xxlJobLog == null) {
			return result;
		}
		for (int i = 1; i <= total; i++) {
			XxlJobSubLog xxlJobSubLog = new XxlJobSubLog();
			xxlJobSubLog.setParentId(xxlJobLog.getId());
			xxlJobSubLog.setJobGroup(xxlJobLog.getJobGroup());
			xxlJobSubLog.setJobId(xxlJobLog.getJobId());
			xxlJobSubLog.setIndex(i);
			if (total != null) {
				xxlJobSubLog.setTotal(total);
			}
			xxlJobSubLog.setExecutorParam("子任务序号：" + i);
			xxlJobSubLog.setTriggerTime(new Date());
			xxlJobSubLog.setTriggerCode(ReturnT.SUCCESS_CODE);
			xxlJobSubLog.setTriggerMsg("任务触发类型：接口触发<br>创建机器：" + ip + "<br>");
			xxlJobSubLogDao.save(xxlJobSubLog);
			result.add(new SubJobInfoForBatchCreate(xxlJobSubLog.getId(), i, total));
		}
		return result;
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
