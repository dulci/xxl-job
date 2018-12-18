package com.xxl.job.executor.service.overseer;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxl.job.api.service.LogService;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dul-c on 2018-12-12.
 */
@Service(
        version = "${xxl.job.overseer.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
@Component
public class LogServiceImpl implements LogService {

    /**
     * Status Report 状态汇报
     *
     * @param taskInstanceId 任务实例ID
     * @param ip             ip
     * @param log            日志信息
     * @return 0：成功
     */
    @Override
    public Integer report(Integer taskInstanceId, String ip, String log) {
        new LogThread(taskInstanceId, ip, log).start();
        return 0;
    }

    private static class LogThread extends Thread {
        private Integer logId;
        private String ip;
        private String log;

        public LogThread(Integer logId, String ip, String log) {
            this.logId = logId;
            this.ip = ip;
            this.log = log;

        }

        public void run() {
            String logFileName = XxlJobFileAppender.makeLogFileName(new Date(), this.logId);
            XxlJobFileAppender.contextHolder.set(logFileName);
            XxlJobLogger.log("ip:{} , detail:{}", ip, log);

        }
    }
}