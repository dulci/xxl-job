package com.xxl.job.api.service;

/**
 * @desc 通过api触发任务
 * Created by zhangjianlong on 2019/1/10.
 *  @return 0：成功 1 :没有找到对应的任务 2：找到多个任务
 */
public interface XxlJobAdminJobInfoTriggerService {
    /**
     * 通过jobId触发任务
     * @param jobId
     * @return 0：成功
     */
    Integer triggerByJobId(Integer jobId,String param);

    /**
     * 通过jobDesc触发任务
     * @param jobDesc
     * @return 0：成功
     */
    Integer triggerByJobDesc(String  jobDesc,String param);

    /**
     * 通过mqKey触发任务
     * @param mqKey
     * @return 0：成功
     */
    Integer triggerByMqKey(String  mqKey,String param);
}
