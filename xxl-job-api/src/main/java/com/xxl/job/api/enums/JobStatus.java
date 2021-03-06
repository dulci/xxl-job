package com.xxl.job.api.enums;

/**
 * Created by dul-c on 2018-12-12.
 */
public enum JobStatus {
	UNSTARTED("0", "未启动"),
	PROCESSING("100", "执行中"),
	PROCESSING_IGNORE_ANY_ERROR("101", "忽略任何错误，并继续执行"),
	SUCCESS("200", "成功"),
	FAIL("500", "失败"),
	FAIL_TIMEOUT("502", "超时"),
	FAIL_IF_OCCUR_ANY_ERROR("401", "发生任何错误，则任务失败"),
	FAIL_IF_OCCUR_SOME_ERROR("402", "发生指定数量的错误，则任务失败"),
	FAIL_IF_OCCUR_PERCENT_ERROR("403", "发生指定百分比的错误，则任务失败");

	JobStatus(String value) {
		this.value = value;
	}
	JobStatus(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	private String value;
	private String desc;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
