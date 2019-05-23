


import com.alibaba.dubbo.config.annotation.Reference;

import com.xxl.job.api.service.StatusService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = XxlJobExecutorTestApplication.class)
public class TestT{
    @Reference(version = "1.0.0")
    private StatusService statusService;

    @Test
    public void test8(){
        System.out.println("测试开始************************！");
        //statusService.report(13563,"",200);
        System.out.println("测试结束*************************！");
    }


}
