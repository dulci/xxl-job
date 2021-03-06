package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.service.impl.AdminBizImpl;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.util.DateUtil;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.ThrowableUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * xxl-job trigger
 * Created by xuxueli on 17/7/13.
 */
public class XxlJobTrigger {
	private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

	/**
	 * trigger job
	 *
	 * @param jobId
	 * @param triggerType
	 * @param failRetryCount        >=0: use this param
	 *                              <0: use param from job info config
	 * @param executorShardingParam
	 * @param executorParam         null: use job param
	 *                              not null: cover job param
	 */
	public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam, String flowInstance) {
		// load data
		XxlJobInfo jobInfo = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().loadById(jobId);
		if (jobInfo == null) {
			logger.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
			return;
		}
		if (executorParam != null) {
			jobInfo.setExecutorParam(executorParam);
		}
		int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();
		XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(jobInfo.getJobGroup());

		// sharding param
		int[] shardingParam = null;
		if (executorShardingParam != null) {
			String[] shardingArr = executorShardingParam.split("/");
			if (shardingArr.length == 2 && StringUtils.isNumeric(shardingArr[0]) && StringUtils.isNumeric(shardingArr[1])) {
				shardingParam = new int[2];
				shardingParam[0] = Integer.valueOf(shardingArr[0]);
				shardingParam[1] = Integer.valueOf(shardingArr[1]);
			}
		}
		if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) && CollectionUtils.isNotEmpty(group
				.getRegistryList()) && shardingParam == null) {
			for (int i = 0; i < group.getRegistryList().size(); i++) {
				processTrigger(group, jobInfo, finalFailRetryCount, triggerType, i, group.getRegistryList().size(), flowInstance);
			}
		} else {
			if (shardingParam == null) {
				shardingParam = new int[]{0, 1};
			}
			processTrigger(group, jobInfo, finalFailRetryCount, triggerType, shardingParam[0], shardingParam[1], flowInstance);
		}

	}

	/**
	 * @param group               job group, registry list may be empty
	 * @param jobInfo
	 * @param finalFailRetryCount
	 * @param triggerType
	 * @param index               sharding index
	 * @param total               sharding index
	 * @param flowInstance        流程实例
	 */
	private static void processTrigger(XxlJobGroup group, XxlJobInfo jobInfo, int finalFailRetryCount, TriggerTypeEnum triggerType, int index, int total, String flowInstance) {

		// param
		ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);  // block
		// strategy
		ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);    // route strategy
		String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) ? String.valueOf(index).concat("/").concat(String.valueOf(total)) :
				null;

		// 1、save log-id
		XxlJobLog jobLog = new XxlJobLog();
		jobLog.setJobGroup(jobInfo.getJobGroup());
		jobLog.setJobId(jobInfo.getId());
		jobLog.setTriggerTime(new Date());
		if (StringUtils.isNotEmpty(flowInstance)) {
			jobLog.setFlowInstance(flowInstance);
		} else {
			jobLog.setFlowInstance(getFlowInstance(jobInfo.getJobCron(), jobInfo.getId()));
		}
		XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().save(jobLog);
		logger.info(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

		// 2、init trigger-param
		TriggerParam triggerParam = new TriggerParam();
		triggerParam.setJobId(jobInfo.getId());
		triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
		triggerParam.setExecutorParams(jobInfo.getExecutorParam());
		triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
		triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
		triggerParam.setLogId(jobLog.getId());
		triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
		triggerParam.setGlueType(jobInfo.getGlueType());
		triggerParam.setGlueSource(jobInfo.getGlueSource());
		triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
		triggerParam.setBroadcastIndex(index);
		triggerParam.setBroadcastTotal(total);
		triggerParam.setMqKey(jobInfo.getMqKey());
		triggerParam.setJobDesc(jobInfo.getJobDesc());
		// 3、init address
		String address = null;
		ReturnT<String> routeAddressResult = null;
		if (CollectionUtils.isNotEmpty(group.getRegistryList())) {
			if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) {
				if (index < group.getRegistryList().size()) {
					address = group.getRegistryList().get(index);
				} else {
					address = group.getRegistryList().get(0);
				}
			} else {
				routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, group.getRegistryList());
				if (routeAddressResult.getCode() == ReturnT.SUCCESS_CODE) {
					address = routeAddressResult.getContent();
				}
			}
		} else {
			routeAddressResult = new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobconf_trigger_address_empty"));
		}

		logger.info(">>>>>>>>>>> xxl-job invokie executor begin, jobId:{}", jobLog.getId());
		// 4、trigger remote executor
		ReturnT<String> triggerResult = null;
		if (address != null) {
			triggerResult = runExecutor(triggerParam, address);
		} else {
			triggerResult = new ReturnT<String>(ReturnT.FAIL_CODE, null);
		}
		logger.info(">>>>>>>>>>> xxl-job invokie executor end, jobId:{}", jobLog.getId());

		// 5、collection trigger info
		logger.info(">>>>>>>>>>> xxl-job collection trigger info begin, jobId:{}", jobLog.getId());
		StringBuffer triggerMsgSb = new StringBuffer();
		triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_type")).append("：").append(triggerType.getTitle());
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IpUtil.getIp());
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regtype")).append("：").append((group.getAddressType() == 0) ? I18nUtil.getString
				("jobgroup_field_addressType_0") : I18nUtil.getString("jobgroup_field_addressType_1"));
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regaddress")).append("：").append(group.getRegistryList());
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum.getTitle());
		if (shardingParam != null) {
			triggerMsgSb.append("(" + shardingParam + ")");
		}
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_timeout")).append("：").append(jobInfo.getExecutorTimeout());
		triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailRetryCount")).append("：").append(finalFailRetryCount);

		triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>" + I18nUtil.getString("jobconf_trigger_run") + "<<<<<<<<<<< </span><br>").append(
				(routeAddressResult != null && routeAddressResult.getMsg() != null) ? routeAddressResult.getMsg() + "<br><br>" : "").append(triggerResult.getMsg() != null ?
				triggerResult.getMsg() : "");
		logger.info(">>>>>>>>>>> xxl-job collection trigger info end, jobId:{}", jobLog.getId());

		// 6、save log trigger-info
		logger.info(">>>>>>>>>>> xxl-job save log trigger-info begin, jobId:{}", jobLog.getId());
		jobLog.setExecutorAddress(address);
		jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
		jobLog.setExecutorParam(jobInfo.getExecutorParam());
		jobLog.setExecutorShardingParam(shardingParam);
		jobLog.setExecutorFailRetryCount(finalFailRetryCount);
		//jobLog.setTriggerTime();
		jobLog.setTriggerCode(triggerResult.getCode());
		jobLog.setTriggerMsg(triggerMsgSb.toString());
		if (jobInfo.getExecutorTimeout() != 0) {
			Date deathLine = DateUtil.calSeconds(new Date(), jobInfo.getExecutorTimeout());
			jobLog.setDeathLine(deathLine);

		}
		XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateTriggerInfo(jobLog);
		logger.info(">>>>>>>>>>> xxl-job save log trigger-info end, jobId:{}", jobLog.getId());

		logger.info(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());
	}

	/**
	 * run executor
	 *
	 * @param triggerParam
	 * @param address
	 * @return
	 */
	public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address) {
		ReturnT<String> runResult = null;
		try {
			ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(address);
			runResult = executorBiz.run(triggerParam);
		} catch (Exception e) {
			logger.error(">>>>>>>>>>> xxl-job trigger error, please check if the executor[{}] is running.", address, e);
			runResult = new ReturnT<String>(ReturnT.FAIL_CODE, ThrowableUtil.toString(e));
		}

		StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
		runResultSB.append("<br>address：").append(address);
		runResultSB.append("<br>code：").append(runResult.getCode());
		runResultSB.append("<br>msg：").append(runResult.getMsg());

		runResult.setMsg(runResultSB.toString());
		return runResult;
	}

	private static String getFlowInstance(String cron, Integer taskId) {
		String c[] = cron.split(" ");
		if (!c[6].equals("*")) {
			return new SimpleDateFormat("yyyy").format(new Date());
		} else if (!c[4].equals("*")) {
			if (c[4].contains("/")) {
				return new SimpleDateFormat("yyyyMM").format(new Date());
			} else {
				return new SimpleDateFormat("yyyy").format(new Date());
			}
		} else if (!c[3].equals("*")) {
			if (c[3].contains("/")) {
				return new SimpleDateFormat("yyyyMMdd").format(new Date());
			} else {
				return new SimpleDateFormat("yyyyMM").format(new Date());
			}
		} else if (!c[2].equals("*")) {
			if (c[2].contains("/")) {
				return new SimpleDateFormat("yyyyMMddHH").format(new Date());
			} else {
				return new SimpleDateFormat("yyyyMMdd").format(new Date());
			}
		} else if (!c[1].equals("*")) {
			if (c[1].contains("/")) {
				return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
			} else {
				return new SimpleDateFormat("yyyyMMddHH").format(new Date());
			}
		} else if (!c[0].equals("*")) {
			if (c[0].contains("/")) {
				return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			} else {
				return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
			}
		} else {
			return String.valueOf(taskId);
		}
	}

}
