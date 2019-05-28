package com.xxl.job.executor.core.model.xxljob;

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
	private Integer parentId;
	private int type;

	// job info
	private int jobGroup;
	private int jobId;

	// subJob info
	private int index;
	private Integer total;

	private String flowInstance;

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
	private Date finishTime;

	private Double percent;

	// alarm info
	private int alarmStatus;

}
