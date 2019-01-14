package com.xxl.job.admin.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.api.service.XxlJobAdminJobInfoTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangjianlong on 2019/1/10.
 */
@Service(

        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
@Component
@Slf4j
public class AddminJobInfoTriggerServiceImpl  implements XxlJobAdminJobInfoTriggerService {
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;

    @Override
    public Integer triggerByJobId(Integer jobId) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
        if(xxlJobInfo == null){
            return 1;
        }
        JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.API, -1, null, null, null);
        return  0;
    }

    @Override
    public Integer triggerByJobDesc(String jobDesc) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobDesc(jobDesc);
        List<XxlJobInfo> xxlJobInfoList = xxlJobInfoDao.selectListByJobInfo(xxlJobInfo);
        if(xxlJobInfoList == null || xxlJobInfoList.size() == 0){
            return  1;
        }
        if(xxlJobInfoList.size()>1){
            return 2;
        }
        JobTriggerPoolHelper.trigger(xxlJobInfoList.get(0).getId(), TriggerTypeEnum.API, -1, null, null, null);
        return 0;

    }

    @Override
    public Integer triggerByMqKey(String mqKey) {

        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setMqKey(mqKey);
        List<XxlJobInfo> xxlJobInfoList = xxlJobInfoDao.selectListByJobInfo(xxlJobInfo);
        if(xxlJobInfoList == null || xxlJobInfoList.size() == 0){
            return  1;
        }
        if(xxlJobInfoList.size()>1){
            return 2;
        }
        JobTriggerPoolHelper.trigger(xxlJobInfoList.get(0).getId(), TriggerTypeEnum.API, -1, null, null, null);
        return 0;
    }
}
