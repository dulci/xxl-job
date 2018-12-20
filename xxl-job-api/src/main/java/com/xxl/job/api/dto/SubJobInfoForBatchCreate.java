package com.xxl.job.api.dto;

/**
 * Created by dul-c on 2018-12-12.
 */
public class SubJobInfoForBatchCreate {
	/**
	 * 子任务ID
	 */
	private Integer taskId = 0;

	/**
	 * 子任务序号
	 */
	private Integer index;

	/**
	 * 子任务总数量
	 */
	private Integer total;

	public SubJobInfoForBatchCreate() {
	}

	public SubJobInfoForBatchCreate(Integer taskId, Integer index, Integer total) {
		this.taskId = taskId;
		this.index = index;
		this.total = total;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
