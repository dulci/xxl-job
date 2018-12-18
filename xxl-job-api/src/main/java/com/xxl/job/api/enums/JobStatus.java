package com.xxl.job.api.enums;

/**
 * Created by dul-c on 2018-12-12.
 */
public enum JobStatus {

	SUCCESS("200"),
	FAIL("500"),
	FAIL_TIMEOUT("502"),
	PROCESSING("100");

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