package com.xxl.job.worker.util;

import com.sun.istack.internal.NotNull;
import com.xxl.job.api.dto.SubJobInfoForBatchCreate;
import com.xxl.job.api.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@AllArgsConstructor
public class BoundWorkerUtil {

	private Integer taskInstanceId;
	private WorkerUtil workerUtil;

	public static BoundWorkerUtil getInstance(@NotNull Integer taskInstanceId, @NotNull WorkerUtil workerUtil) {
		if (taskInstanceId == null) {
			throw new RuntimeException("taskInstanceId should not be null!");
		}
		if (workerUtil == null) {
			throw new RuntimeException("workerUtil should not be null!");
		}
		return new BoundWorkerUtil(taskInstanceId, workerUtil);
	}

	public Integer reportLog(String log, Object... args) {
		return workerUtil.reportLog(taskInstanceId, format(log, args));
	}

	public Integer reportException(Exception e) {
		return workerUtil.reportException(taskInstanceId, e);
	}

	public Integer reportPercent(Double percent) {
		return workerUtil.reportPercent(taskInstanceId, percent);
	}

	public Integer reportStatus(Integer status) {
		return workerUtil.reportStatus(taskInstanceId, status, null);
	}

	public Integer reportStatus(Integer status, String msg, Object... args) {
		return workerUtil.reportStatus(taskInstanceId, status, format(msg, args));
	}

	public Integer reportStatus(JobStatus jobStatus) {
		return workerUtil.reportStatus(taskInstanceId, jobStatus, null);
	}

	public Integer reportStatus(JobStatus jobStatus, String msg, Object... args) {
		return workerUtil.reportStatus(taskInstanceId, jobStatus, format(msg, args));
	}

	public Integer createSubJob(Integer mainTaskInstanceId, Integer index) {
		return workerUtil.createSubJob(mainTaskInstanceId, index);
	}

	public Integer createFinish(Integer mainTaskInstanceId) {
		return workerUtil.createFinish(mainTaskInstanceId);
	}

	public Integer createSubJob(Integer mainTaskInstanceId, Integer index, Integer total) {
		return workerUtil.createSubJob(mainTaskInstanceId, index, total);
	}

	public List<SubJobInfoForBatchCreate> batchCreateSubJob(Integer mainTaskInstanceId, Integer total) {
		return workerUtil.batchCreateSubJob(mainTaskInstanceId, total);
	}

	public Integer isContinueProcess(Integer taskInstanceId) {
		return workerUtil.isContinueProcess(taskInstanceId);
	}

	public static String format(String template, Object... params) {
		for (Object param : params) {
			template = StringUtils.replace(template, "{}", param == null ? null : String.valueOf(param), 1);
		}
		return template;
	}

}
