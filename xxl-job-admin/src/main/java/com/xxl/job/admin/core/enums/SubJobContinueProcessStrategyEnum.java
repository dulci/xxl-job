package com.xxl.job.admin.core.enums;

public enum SubJobContinueProcessStrategyEnum {

	PROCESSING_IGNORE_ANY_ERROR("101", "忽略任何错误，并继续执行"),
	FAIL_IF_OCCUR_ANY_ERROR("401", "发生任何错误，则任务失败"),
	FAIL_IF_OCCUR_SOME_ERROR("402", "发生指定数量的错误，则任务失败"),
	FAIL_IF_OCCUR_PERCENT_ERROR("403", "发生指定百分比的错误，则任务失败");

	SubJobContinueProcessStrategyEnum(String value) {
		this.value = value;
	}
	SubJobContinueProcessStrategyEnum(String value, String desc) {
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
