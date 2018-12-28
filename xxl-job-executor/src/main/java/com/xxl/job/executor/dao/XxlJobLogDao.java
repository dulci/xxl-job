package com.xxl.job.executor.dao;

import com.xxl.job.executor.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

	public List<XxlJobLog> pageList(@Param("offset") int offset,
	                                @Param("pagesize") int pagesize,
	                                @Param("jobGroup") int jobGroup,
	                                @Param("jobId") int jobId,
	                                @Param("triggerTimeStart") Date triggerTimeStart,
	                                @Param("triggerTimeEnd") Date triggerTimeEnd,
	                                @Param("logStatus") int logStatus);

	public int pageListCount(@Param("offset") int offset,
	                         @Param("pagesize") int pagesize,
	                         @Param("jobGroup") int jobGroup,
	                         @Param("jobId") int jobId,
	                         @Param("triggerTimeStart") Date triggerTimeStart,
	                         @Param("triggerTimeEnd") Date triggerTimeEnd,
	                         @Param("logStatus") int logStatus);

	public XxlJobLog load(@Param("id") int id);

	public int save(XxlJobLog xxlJobLog);

	public int updateTriggerInfo(XxlJobLog xxlJobLog);

	public int updateHandleInfo(XxlJobLog xxlJobLog);

	public int delete(@Param("jobId") int jobId);

	public int triggerCountByHandleCode(@Param("handleCode") int handleCode);

	public List<Map<String, Object>> triggerCountByDay(@Param("from") Date from,
	                                                   @Param("to") Date to);

	public int selectCountByParentId(@Param("parentId") int parentId, @Param("handleCode") Integer handleCode);

	public int clearLog(@Param("jobGroup") int jobGroup,
	                    @Param("jobId") int jobId,
	                    @Param("clearBeforeTime") Date clearBeforeTime,
	                    @Param("clearBeforeNum") int clearBeforeNum);

	public List<Integer> findFailJobLogIds(@Param("pagesize") int pagesize);

	public int updateTotalByPercentId(@Param("parentId") int parentId,
	                                  @Param("total") Integer total);

	public int updateAlarmStatus(@Param("logId") int logId,
	                             @Param("oldAlarmStatus") int oldAlarmStatus,
	                             @Param("newAlarmStatus") int newAlarmStatus);

	public int updatePercent(@Param("logId") int logId,
	                         @Param("percent") Double percent);

}
