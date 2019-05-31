package com.xxl.job.executor;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
public class XxlSQLJobExecutorApplication {

	public static void main(String[] args) {
//		SpringApplication.run(XxlJobExecutorApplication.class, args);
		new SpringApplicationBuilder(XxlSQLJobExecutorApplication.class).web(WebApplicationType.NONE).run(args);
	}

}