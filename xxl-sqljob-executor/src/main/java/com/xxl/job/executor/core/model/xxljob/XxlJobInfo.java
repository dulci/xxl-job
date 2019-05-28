package com.xxl.job.executor.core.model.xxljob;

import lombok.Data;

import java.util.Date;

/**
 * xxl-job info
 *
 * @author xuxueli  2016-1-12 18:25:49
 */
@Data
public class XxlJobInfo {
	
	private int id;				// 主键ID	    (JobKey.name)
	
	private int jobGroup;		// 执行器主键ID	(JobKey.group)
	private String jobCron;		// 任务执行CRON表达式 【base on quartz】
	private String jobDesc;
	
	private Date addTime;
	private Date updateTime;
	
	private String author;		// 负责人
	private String alarmEmail;	// 报警邮件
	private String alarmTel;//报警电话

	private String executorRouteStrategy;	// 执行器路由策略
	private String executorHandler;		    // 执行器，任务Handler名称
	private String executorParam;		    // 执行器，任务参数
	private String executorBlockStrategy;	// 阻塞处理策略
	private int executorTimeout;     		// 任务执行超时时间，单位秒
	private int executorFailRetryCount;		// 失败重试次数
	private  String mqKey; // mq键值
	private String datasource; // 数据源
	private String executorSQL; // 执行SQL
	
	private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
	private String glueSource;		// GLUE源代码
	private String glueRemark;		// GLUE备注
	private Date glueUpdatetime;	// GLUE更新时间

	private String childJobId;		// 子任务ID，多个逗号分隔
	private Integer continueProcessStrategy;  // 执行策略（子任务）
	private Double continueProcessValue; // 执行策略触发值（子任务）

	// copy from quartz
	private String jobStatus;		// 任务状态 【base on quartz】

}
