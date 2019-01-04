package com.xxl.job.admin.controller;

import com.xxl.job.admin.service.XxlJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/flowchart")
public class JobFlowChartController {
	private static Logger logger = LoggerFactory.getLogger(JobFlowChartController.class);

	@Resource
	private XxlJobService xxlJobService;

	@RequestMapping
	public String index(Model model, @RequestParam(required = false, defaultValue = "0") Integer jobId) {

		// 执行器列表
		model.addAttribute("jobId", jobId);
		return "flowchart/flowchart.index";
	}
	@RequestMapping("data")
	@ResponseBody
	public Map<String,Object> data(Model model, @RequestParam(required = false, defaultValue = "0") Integer jobId) {

		// 执行器列表
		return	xxlJobService.selectFlowChartData(jobId);

	}

}
