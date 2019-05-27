package com.xxl.job.executor.dto;

import com.xxl.job.api.enums.JobStatus;
import lombok.Data;

/**
 * Created by dul-c on 2018-12-19.
 */
@Data
public class MainJobCallbackInfo {
	private Integer status = Integer.valueOf(JobStatus.UNSTARTED.getValue());
	private Integer taskInstanceId;
}
