package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.ScheduleService;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import com.xxl.job.executor.dao.XxlJobLogDao;
import lombok.extern.slf4j.Slf4j;
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
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private XxlJobLogDao xxlJobLogDao;

	/**
	 * Schedule Report 进度汇报
	 *
	 * @param taskInstanceId 任务实例ID
	 * @param ip             ip
	 * @param percent        百分比
	 * @return 0：继续执行，1：中止
	 */
	@Override
	public Integer report(Integer taskInstanceId, String ip, Double percent) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.format(new Date())).append(" recieve schedule report ").append("[" + taskInstanceId + "]").append("[" + ip + "]").append(" ").append(percent);
		this.log.info(stringBuffer.toString());
		String logFileName = XxlJobFileAppender.makeLogFileName(new Date(), taskInstanceId);
		XxlJobFileAppender.contextHolder.set(logFileName);
		XxlJobLogger.log("ip:{} , task current percent is :{}", ip, percent);

		xxlJobLogDao.updatePercent(taskInstanceId, percent);

		// TODO
		// 策略1：存在失败就停止
		// 策略2：存在多少个失败停止
		// 策略3：大于多少百分比失败停止
		// 策略4：永不停止
		return 0;
	}
}
