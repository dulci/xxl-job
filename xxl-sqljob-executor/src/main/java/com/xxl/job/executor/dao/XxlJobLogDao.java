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

	List<XxlJobLog> pageList(@Param("offset") int offset,
	                                @Param("pagesize") int pagesize,
	                                @Param("jobGroup") int jobGroup,
	                                @Param("jobId") int jobId,
	                                @Param("triggerTimeStart") Date triggerTimeStart,
	                                @Param("triggerTimeEnd") Date triggerTimeEnd,
	                                @Param("logStatus") int logStatus);

	int pageListCount(@Param("offset") int offset,
	                         @Param("pagesize") int pagesize,
	                         @Param("jobGroup") int jobGroup,
	                         @Param("jobId") int jobId,
	                         @Param("triggerTimeStart") Date triggerTimeStart,
	                         @Param("triggerTimeEnd") Date triggerTimeEnd,
	                         @Param("logStatus") int logStatus);

	XxlJobLog load(@Param("id") int id);

	int save(XxlJobLog xxlJobLog);

	int updateTriggerInfo(XxlJobLog xxlJobLog);

	int updateHandleInfo(XxlJobLog xxlJobLog);

	int delete(@Param("jobId") int jobId);

	int triggerCountByHandleCode(@Param("handleCode") int handleCode);

	List<Map<String, Object>> triggerCountByDay(@Param("from") Date from,
	                                                   @Param("to") Date to);

	int selectCountByParentId(@Param("parentId") int parentId, @Param("handleCode") Integer handleCode);

	int clearLog(@Param("jobGroup") int jobGroup,
	                    @Param("jobId") int jobId,
	                    @Param("clearBeforeTime") Date clearBeforeTime,
	                    @Param("clearBeforeNum") int clearBeforeNum);

	List<Integer> findFailJobLogIds(@Param("pagesize") int pagesize);

	int updateTotalByPercentId(@Param("parentId") int parentId, @Param("total") Integer total);

	int updateAlarmStatus(@Param("logId") int logId,
	                             @Param("oldAlarmStatus") int oldAlarmStatus,
	                             @Param("newAlarmStatus") int newAlarmStatus);

	int updatePercent(@Param("logId") int logId, @Param("percent") Double percent);

}
