package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.LogInfoUtil;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 跨平台RabbitMQ任务
 *
 * @author zhangjl-h
 */
@JobHandler(value = "rabbitMQJobHandler")
@Component
public class RabbitMQJobHandler extends IJobHandler {
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			Integer logId = LogInfoUtil.getLogId();
			String mqKey = LogInfoUtil.getMqKey();
			Integer jobId = LogInfoUtil.getJobId();
			String jobDesc = LogInfoUtil.getJobDesc();
			XxlJobLogger.log("logId({});mqkey({});param({}) ;jobId:({});jobDesc :({})", logId, mqKey, param, jobId, jobDesc);
			Map<String, Object> map = new HashMap<>();
			map.put("taskInstanceId", logId);
			map.put("param", param);
			map.put("jobId", jobId);
			map.put("jobDesc", jobDesc);
			amqpTemplate.convertAndSend(mqKey, map);
			XxlJobLogger.log("RabbitMQ({}) succsss", param);
		} catch (AmqpException e) {
			e.printStackTrace();
			XxlJobLogger.log("RabbitMQ({}) fail", param);
			XxlJobLogger.log(e);
			return FAIL;
		}
		return NO_START;
	}

}
