package com.xxl.job.admin.core.model;

import lombok.Data;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 *
 * @author xuxueli  2015-12-19 23:19:09
 */
@Data
public class XxlJobLog {

	private Integer id;
	private Integer parentId;
	private Integer type;

	// job info
	private Integer jobGroup;
	private Integer jobId;

	// subJob info
	private Integer index;
	private Integer total;

	// execute info
	private String executorAddress;
	private String executorHandler;
	private String executorParam;
	private String executorShardingParam;
	private Integer executorFailRetryCount;

	// trigger info
	private Date triggerTime;
	private Integer triggerCode;
	private String triggerMsg;

	// handle info
	private Date handleTime;
	private Integer handleCode;
	private String handleMsg;

	// alarm info
	private Integer alarmStatus;

}
