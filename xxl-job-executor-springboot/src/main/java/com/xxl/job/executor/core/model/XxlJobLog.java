package com.xxl.job.executor.core.model;

import lombok.Data;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 *
 * @author xuxueli  2015-12-19 23:19:09
 */
@Data
public class XxlJobLog {

	private int id;
	private int parentId;
	private int type;

	// job info
	private int jobGroup;
	private int jobId;

	// subJob info
	private int index;
	private int total;

	// execute info
	private String executorAddress;
	private String executorHandler;
	private String executorParam;
	private String executorShardingParam;
	private int executorFailRetryCount;

	// trigger info
	private Date triggerTime;
	private int triggerCode;
	private String triggerMsg;

	// handle info
	private Date handleTime;
	private int handleCode;
	private String handleMsg;

	// alarm info
	private int alarmStatus;

}
