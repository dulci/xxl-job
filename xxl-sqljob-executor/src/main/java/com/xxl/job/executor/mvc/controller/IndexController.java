package com.xxl.job.executor.mvc.controller;

import com.xxl.job.api.service.LogService;
import com.xxl.job.api.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	@RequestMapping("/")
	@ResponseBody
	String index() {
		return "xxl job executor running.";
	}
}