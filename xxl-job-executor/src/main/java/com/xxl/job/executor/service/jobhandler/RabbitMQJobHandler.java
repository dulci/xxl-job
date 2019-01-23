package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.thread.TriggerCallbackThread;
import com.xxl.job.core.util.LogInfoUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
		new SendMessageThread(param).start();
		return NO_START;
	}

	private class SendMessageThread extends Thread {

		private String param;

		public SendMessageThread(String param) {
			this.param = param;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
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
			} catch (InterruptedException e) {
				e.printStackTrace();
				XxlJobLogger.log("RabbitMQ({}) send message fail", param);
				ReturnT result = new ReturnT(ReturnT.FAIL.getCode(), "RabbitMQ({}) send message fail");
				TriggerCallbackThread.pushCallBack(new HandleCallbackParam(LogInfoUtil.getLogId(), System.currentTimeMillis(), result));
			}
		}
	}
}
