package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao {

	public List<XxlJobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("jobDesc") String jobDesc,
									 @Param("mqKey") String mqKey,
									 @Param("jobSystem") String jobSystem,
									 @Param("jobModule") String jobModule,
									 @Param("excludeIdList")  List<Integer> excludeIdList);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("jobDesc") String jobDesc,
							 @Param("mqKey") String mqKey,
							 @Param("jobSystem") String jobSystem,
							 @Param("jobModule") String jobModule,
							 @Param("excludeIdList")  List<Integer> excludeIdList);
	
	public int save(XxlJobInfo info);

	public XxlJobInfo loadById(@Param("id") int id);
	
	public int update(XxlJobInfo item);
	
	public int delete(@Param("id") int id);

	public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	List<XxlJobInfo> findJobsByChildJobId(@Param("id") Integer id, @Param("jobidRegexp") String jobidRegexp);

}
