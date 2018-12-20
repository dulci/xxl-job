package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.enums.JobStatus;
import com.xxl.job.api.service.StatusService;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.thread.TriggerCallbackThread;
import com.xxl.job.core.util.DateUtil;
import com.xxl.job.executor.core.model.XxlJobLog;
import com.xxl.job.executor.dao.XxlJobLogDao;
import com.xxl.job.executor.dto.MainJobCallbackInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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
public class StatusServiceImpl implements StatusService {
	@Autowired
	private XxlJobLogDao xxlJobLogDao;

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param status         状态值
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, Integer status, String msg) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve status report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(status).append(" ").append(msg);
		this.log.info(stringBuffer.toString());

		XxlJobLog xxlJobLog = xxlJobLogDao.load(taskInstanceId);
		if (xxlJobLog == null) {
			return 0;
		}

		// 当前任务数据更新
		jobDataUpdate(xxlJobLog, status, ip, msg);

		// 更新主任务进度及状态
		MainJobCallbackInfo mainJobCallbackInfo = updateMainJobPersentAndStatus(xxlJobLog, status);

		// 主任务状态通知Admin
		if (!mainJobCallbackInfo.getStatus().equals(JobStatus.UNSTARTED) && mainJobCallbackInfo.getTaskInstanceId() != null) {
			ReturnT result = new ReturnT(mainJobCallbackInfo.getStatus(), msg);
			TriggerCallbackThread.pushCallBack(new HandleCallbackParam(mainJobCallbackInfo.getTaskInstanceId(), System.currentTimeMillis(), result));
		}
		return 0;
	}

	/**
	 * Status Report 状态汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param jobStatus      状态值
	 * @return 0：成功
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, JobStatus jobStatus, String msg) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve status report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(jobStatus.getValue()).append(" ").append(msg);
		this.log.info(stringBuffer.toString());

		return report(taskInstanceId, ip, Integer.valueOf(jobStatus.getValue()), msg);
	}

	/**
	 * 当前任务数据更新（状态、执行时间、执行备注、重复执行次数）
	 */
	private void jobDataUpdate(XxlJobLog xxlJobLog, Integer status, String ip, String msg) {
		if (status.equals(Integer.valueOf(JobStatus.PROCESSING.getValue()))) {
			xxlJobLog.setHandleTime(new Date());
			if (StringUtils.isNotEmpty(msg)) {
				xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg() + "<br>" + msg);
			} else {
				xxlJobLog.setHandleMsg("执行机器IP:" + ip);
			}
			if (Integer.valueOf(JobStatus.PROCESSING.getValue()).equals(xxlJobLog.getHandleCode())) {
				xxlJobLog.setExecutorFailRetryCount(xxlJobLog.getExecutorFailRetryCount() + 1);
			}
		}
		if (xxlJobLog.getType() == 2) {
			xxlJobLog.setHandleCode(status);
		}
		if (status.equals(Integer.valueOf(JobStatus.SUCCESS.getValue()))) {
			xxlJobLog.setPersent(100.0);
		}
		xxlJobLogDao.updateTriggerInfo(xxlJobLog);
	}

	/**
	 * 更新主任务进度及状态
	 */
	private MainJobCallbackInfo updateMainJobPersentAndStatus(XxlJobLog xxlJobLog, Integer status) {
		MainJobCallbackInfo result = new MainJobCallbackInfo();
		XxlJobLog mainJobLog = xxlJobLogDao.load(xxlJobLog.getParentId());
		if (xxlJobLog.getType() == 1) {//主任务汇报
			result.setStatus(status);
			result.setTaskInstanceId(xxlJobLog.getId());
		} else if (status.equals(Integer.valueOf(JobStatus.SUCCESS.getValue()))) {//子任务执行成功
			Integer total = xxlJobLog.getTotal();
			if (xxlJobLog.getTotal() == 0) {
				total = xxlJobLogDao.selectCountByParentId(xxlJobLog.getParentId(), null);
			}
			Integer finish = xxlJobLogDao.selectCountByParentId(xxlJobLog.getParentId(), Integer.valueOf(JobStatus.SUCCESS.getValue()));
			Double persent = (Double.valueOf(finish)) / Double.valueOf(total) * 100.0;

			if (mainJobLog != null) {
				// 更新主任务进度
				mainJobLog.setPersent(persent);

				// 更新主任务状态(只更新不是子任务的主任务的状态)
				if ((finish == total) && mainJobLog.getType() == 2) {
					mainJobLog.setTriggerCode(Integer.valueOf(JobStatus.SUCCESS.getValue()));
				}
				xxlJobLogDao.updateTriggerInfo(mainJobLog);

				if (finish == total) {
					if (mainJobLog.getType() == 2) {
						// 若主任务仍然是子任务则进行递归调用
						return updateMainJobPersentAndStatus(mainJobLog, Integer.valueOf(JobStatus.SUCCESS.getValue()));
					} else {
						// 若主任务不是子任务则进行递归调用
						result.setStatus(Integer.valueOf(JobStatus.SUCCESS.getValue()));
						result.setTaskInstanceId(mainJobLog.getId());
					}
				}
			}
		} else if (status.equals(Integer.valueOf(JobStatus.FAIL.getValue()))) {//子任务执行失败
			if (mainJobLog != null) {
				if (mainJobLog.getType() == 2) {
					// 若主任务仍然是子任务则进行递归调用
					mainJobLog.setHandleCode(Integer.valueOf(JobStatus.FAIL.getValue()));
					xxlJobLogDao.updateTriggerInfo(mainJobLog);
				} else {
					// 若主任务不是子任务则进行递归调用
					result.setStatus(Integer.valueOf(JobStatus.FAIL.getValue()));
					result.setTaskInstanceId(mainJobLog.getId());
				}
			}
		}
		return result;
	}
}
