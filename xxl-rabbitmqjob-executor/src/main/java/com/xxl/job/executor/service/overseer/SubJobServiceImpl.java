package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.api.service.SubJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import com.xxl.job.executor.core.model.XxlJobInfo;
import com.xxl.job.executor.core.model.XxlJobLog;
import com.xxl.job.executor.dao.XxlJobInfoDao;
import com.xxl.job.executor.dao.XxlJobLogDao;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SubJobServiceImpl implements SubJobService {
	@Autowired
	private XxlJobLogDao xxlJobLogDao;
	@Autowired
	private XxlJobInfoDao xxlJobInfoDao;

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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve subjob creating application ").append("[" + mainTaskInstanceId + "]").append("[" + ip + "]").append(" ").append(index);
		log.info(stringBuffer.toString());

		return create(mainTaskInstanceId, ip, index, null);
	}

	/**
	 * SubJob createFinish 创建子任务完成
	 *
	 * @param mainTaskInstanceId 主任务实例ID
	 * @return 0：成功
	 */
	@Override
	public Integer createFinish(Integer mainTaskInstanceId) {
		Integer total = xxlJobLogDao.selectCountByParentId(mainTaskInstanceId, null);
		xxlJobLogDao.updateTotalByPercentId(mainTaskInstanceId, total);
		return 0;
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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve subjob creating application ").append("[" + mainTaskInstanceId + "]").append("[" + ip + "]").append(" ").append(index).append(" ").append(total);
		log.info(stringBuffer.toString());

		XxlJobLog xxlJobLog = xxlJobLogDao.load(mainTaskInstanceId);
		if (xxlJobLog == null) {
			return 0;
		}
		return saveXxlJobSubLog(xxlJobLog, ip, index, total);
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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve subjob batch creating application ").append("[" + mainTaskInstanceId + "]").append("[" + ip + "]").append(" ").append(total);
		log.info(stringBuffer.toString());

		List<SubJobInfoForBatchCreate> result = new ArrayList<>();
		XxlJobLog xxlJobLog = xxlJobLogDao.load(mainTaskInstanceId);
		if (xxlJobLog == null) {
			return result;
		}
		for (int i = 1; i <= total; i++) {
			result.add(new SubJobInfoForBatchCreate(saveXxlJobSubLog(xxlJobLog, ip, i, total), i, total));
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
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve subjob continuing asking ").append("[" + taskInstanceId + "]");
		log.info(stringBuffer.toString());

		XxlJobLog xxlJobSubLog = xxlJobLogDao.load(taskInstanceId);
		if (xxlJobSubLog != null && xxlJobSubLog.getParentId() != null) {
			XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(xxlJobSubLog.getJobId());
			if (xxlJobInfo.getContinueProcessStrategy() != null) {
				JobStatus jobStatus = JobStatus.valueOf(xxlJobInfo.getContinueProcessStrategy().toString());
				switch (jobStatus) {
					// 策略1：存在失败就停止
					case FAIL_IF_OCCUR_ANY_ERROR:
						return 1;
					// 策略2：存在多少个失败停止
					case FAIL_IF_OCCUR_SOME_ERROR:
						if (xxlJobInfo.getContinueProcessValue() != null) {
							if (countErrorSubJob(xxlJobSubLog.getParentId()) >= xxlJobInfo.getContinueProcessValue()) {
								return 1;
							}
						}
						break;
					// 策略3：大于多少百分比失败停止
					case FAIL_IF_OCCUR_PERCENT_ERROR:
						if (xxlJobInfo.getContinueProcessValue() != null && xxlJobSubLog.getTotal() != null) {
							double errorPercent = countErrorSubJob(xxlJobSubLog.getParentId()) / (double) xxlJobSubLog.getTotal();
							if (errorPercent >= xxlJobInfo.getContinueProcessValue()) {
								return 1;
							}
						}
						break;
					// 策略4：永不停止
					case PROCESSING_IGNORE_ANY_ERROR:
						break;
				}
			}
		}
		return 0;
	}

	private Integer saveXxlJobSubLog(XxlJobLog xxlJobLog, String ip, Integer index, Integer total) {
		XxlJobLog xxlJobSubLog = new XxlJobLog();
		xxlJobSubLog.setParentId(xxlJobLog.getId());
		xxlJobSubLog.setType(2);
		xxlJobSubLog.setJobGroup(xxlJobLog.getJobGroup());
		xxlJobSubLog.setJobId(xxlJobLog.getJobId());
		xxlJobSubLog.setFlowInstance(xxlJobLog.getFlowInstance());
		xxlJobSubLog.setIndex(index);
		if (total != null) {
			xxlJobSubLog.setTotal(total);
		}
		xxlJobSubLog.setExecutorAddress(xxlJobLog.getExecutorAddress());
		xxlJobSubLog.setExecutorParam("任务触发类型：接口触发<br>创建机器：" + ip + "<br>子任务序号：" + index);
		xxlJobSubLog.setTriggerTime(new Date());
		xxlJobSubLog.setTriggerCode(ReturnT.SUCCESS_CODE);
		xxlJobSubLog.setTriggerMsg("任务触发类型：接口触发<br>创建机器：" + ip + "<br>子任务序号：" + index);
		xxlJobSubLog.setHandleCode(Integer.valueOf(JobStatus.UNSTARTED.getValue()));
		xxlJobLogDao.save(xxlJobSubLog);
		return xxlJobSubLog.getId();
	}

	/**
	 * 获得失败的子任务数量
	 */
	private int countErrorSubJob(Integer parentId) {


		int count = 0;
		count += xxlJobLogDao.selectCountByParentId(parentId, Integer.valueOf(JobStatus.FAIL.getValue()));
		count += xxlJobLogDao.selectCountByParentId(parentId, Integer.valueOf(JobStatus.FAIL_TIMEOUT.getValue()));
		return count;
	}

}
