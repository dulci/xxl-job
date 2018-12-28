package com.xxl.job.api.enums;

/**
 * Created by dul-c on 2018-12-12.
 */
public enum JobStatus {
	UNSTARTED("0"),
	PROCESSING("100"),
	PROCESSING_IGNORE_ANY_ERROR("101"),
	SUCCESS("200"),
	FAIL("500"),
	FAIL_TIMEOUT("502"),
	FAIL_IF_OCCUR_ANY_ERROR("401"),
	FAIL_IF_OCCUR_SOME_ERROR("402"),
	FAIL_IF_OCCUR_PERCENT_ERROR("403");

	private JobStatus(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
