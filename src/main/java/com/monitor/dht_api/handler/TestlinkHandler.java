package com.monitor.dht_api.handler;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Reporter;

import com.monitor.dht_api.util.ResourceUtil;
import com.monitor.dht_api.util.TestlinkUtil;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;


public class TestlinkHandler {
	private static Logger logger = Logger.getLogger(TestlinkHandler.class);
	private static String testproject_name = ResourceUtil.getKey("testproject_name");
	private static String testplan_name = ResourceUtil.getKey("testplan_name");
	private static String testsuite_name = ResourceUtil.getKey("testsuite_name");
	

	
	public static void testlinkExecute() {
		TestLinkAPI api = TestlinkUtil.connect();
		logger.info("已经成功连接testlink");
		TestProject pro = TestlinkUtil.getTestProject(api, testproject_name);
		logger.info("测试产品名称："+testproject_name);
		Reporter.log("测试产品名称："+testproject_name);
		TestPlan plan = TestlinkUtil.getTestPlan(api, pro, testplan_name);
		logger.info("测试计划名称："+testplan_name);
		Reporter.log("测试计划名称："+testplan_name);
		logger.info("测试套件名称："+testsuite_name);
		Reporter.log("测试套件名称："+testsuite_name);
		TestCase[] cases = TestlinkUtil.getTestCasesBySuiteName(api, plan, testsuite_name);
		
		if(cases!=null) {			
			Build buildTest = api.createBuild(plan.getId(), "自动化测试构建_"+ResourceUtil.getCurrentTime(), "天梯app接口测试");
			
			for(int i=0;i<cases.length;i++) {
	        	TestCase testCase = (TestCase)cases[i];
	        	logger.info("测试用例名称："+cases[i].getName());
	        	Reporter.log("测试用例名称："+cases[i].getName());
	        	String caseStep = TestlinkUtil.getStepByCase(api, testCase);
	        	logger.info("测试操作步骤："+caseStep);
	        	Reporter.log("测试操作步骤："+caseStep);
	        	String expectResult = TestlinkUtil.getExpectResultByCase(api, testCase);
	        	logger.info("期望结果："+expectResult);
	        	Reporter.log("期望结果："+expectResult);
	        	String method_str=TestlinkUtil.getJsonStrAttr(caseStep, "method");
	        	logger.info("请求方式："+method_str);
	        	Reporter.log("请求方式："+method_str);
	    		String url_str=TestlinkUtil.getJsonStrAttr(caseStep, "url");
	    		logger.info("请求url："+url_str);
	        	Reporter.log("请求url："+url_str);
	    		String contentType_str=TestlinkUtil.getJsonStrAttr(caseStep, "contentType");
	    		logger.info("请求头参数contentType："+contentType_str);
	        	Reporter.log("请求头参数contentType："+contentType_str);
	    		String body_str = TestlinkUtil.getJsonAttr(caseStep, "body").toString();
	    		logger.info("请求body体："+body_str);
	        	Reporter.log("请求body体："+body_str);
	    		String statusCode_str = TestlinkUtil.getJsonStrAttr(expectResult, "statusCode");
	    		logger.info("期望的返回状态码："+statusCode_str);
	        	Reporter.log("期望的返回状态码："+statusCode_str);	    		
	    		String caseResult = DhtHandler.queryVpnServer_url(method_str, contentType_str, url_str, body_str, statusCode_str);
	    		logger.info("测试结果："+caseResult);
	        	Reporter.log("测试结果："+caseResult);
	        	
	        	try {
					TestlinkUtil.reportResult(pro.getName(), plan.getName(), testCase.getName(), buildTest.getName(), "", caseResult);
					logger.info("写入测试结果成功！");
		        	Reporter.log("写入测试结果成功！");
				} catch (TestLinkAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.info("写入测试结果失败！");
		        	Reporter.log("写入测试结果失败！");
				}
	        }
				
						
		}
        
	}

}
