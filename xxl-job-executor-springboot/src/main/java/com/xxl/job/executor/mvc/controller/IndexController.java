package com.xxl.job.executor.mvc.controller;

import com.xxl.job.api.service.LogService;
import com.xxl.job.api.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class IndexController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private LogService logService;

    @RequestMapping("/")
    @ResponseBody
    String index() {
        return "xxl job executor running.";
    }

    @RequestMapping("/report")
    @ResponseBody
    String report(Integer logId,Integer code) {
        statusService.report(logId,"",code,"");
        return "success";
    }

    @RequestMapping("/logReport")
    @ResponseBody
    String logReport(Integer logId,String log) {
        logService.report(logId,"",log);
        return "success";
    }

}