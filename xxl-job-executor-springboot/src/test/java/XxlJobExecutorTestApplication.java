import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.xxl.job.executor.XxlJobExecutorApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@DubboComponentScan("com.xxl.job.api.service")
@ServletComponentScan
@SpringBootApplication
@ComponentScan({"com.xxl.job.api.service"})

public class XxlJobExecutorTestApplication {

	public static void main(String[] args) {
//		SpringApplication.run(XxlJobExecutorApplication.class, args);
		new SpringApplicationBuilder(XxlJobExecutorTestApplication.class).web(WebApplicationType.NONE).run(args);
	}

}