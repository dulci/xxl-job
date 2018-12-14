package com.xxl.job.api.enums;

/**
 * Created by dul-c on 2018-12-12.
 */
public enum JobStatus {
	UNSTARTED(0),
	SUCCESS(1),
	PROCESSING(2),
	FAIL(3),
	NO_DATA(4);

	private JobStatus(Integer value) {
		this.value = value;
	}

	private Integer value;
}
