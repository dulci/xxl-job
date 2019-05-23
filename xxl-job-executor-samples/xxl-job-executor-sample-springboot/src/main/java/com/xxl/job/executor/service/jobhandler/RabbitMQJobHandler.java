package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 命令行任务
 *
 * @author xuxueli 2018-09-16 03:48:34
 */
@JobHandler(value="rabbitMQJobHandler")
@Component
public class RabbitMQJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {


            return IJobHandler.PROCESS;

    }

}
