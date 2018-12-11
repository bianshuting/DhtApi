package com.monitor.dht_api.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import br.eti.kinoshita.testlinkjavaapi.model.*;

 
public class TestlinkUtil {
	
	/**
	 * 连接testlink方法
	 */
	public static TestLinkAPI connect() {		
		String url = ResourceUtil.getKey("testlink_url");
	    String devKey = ResourceUtil.getKey("testlink_key");
	    TestLinkAPI api = null;	     
	    URL testlinkURL = null;
	     
	    try {
	         testlinkURL = new URL(url);
	    } catch ( MalformedURLException mue )   {
	            mue.printStackTrace( System.err );
	            System.exit(-1);
	    }
	     
	    try {	    	
	        api = new TestLinkAPI(testlinkURL, devKey);		        
	    } catch( Exception te) {
	            te.printStackTrace( System.err );
	            System.exit(-1);
	    }
	     
	    //System.out.println(api.ping());
		return api;
	}
	
	/**
	 * 获取指定的测试产品
	 */
	
	public static TestProject getTestProject(TestLinkAPI api,String projectName) {
		TestProject pro = null;
		TestProject[] projects = api.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if(projects[i].getName().equals(projectName)) {
				return projects[i];
			}
		}
		return pro;
	}
	
	/**
	 * 获取指定的测试计划
	 */
	
	public static TestPlan getTestPlan(TestLinkAPI api,TestProject project,String planName) {
		TestPlan plan = null;
		TestPlan[] plans = api.getProjectTestPlans(project.getId());
		for (int i = 0; i < plans.length; i++) {
			if(plans[i].getName().equals(planName)) {
				return plans[i];
			}
		}
		return plan;
	}
	
	/**
	 * 获取指定的测试产品下的顶层测试套件
	 */
	public static TestSuite[] getTestSuites(TestLinkAPI api,TestProject project) {
		TestSuite[] suites = null;
		suites = api.getFirstLevelTestSuitesForTestProject(project.getId());		
		return suites;
	}
	/**
	 * 获取指定的测试计划下的测试套件
	 */
	public static TestSuite[] getTestSuites(TestLinkAPI api,TestPlan plan) {
		TestSuite[] suites = null;
		suites = api.getTestSuitesForTestPlan(plan.getId());
		return suites;
	}
	/**
	 * 获取指定的计划下的所有测试套件下的测试用例集
	 */
	public static HashMap<String,TestCase[]> getTestCases(TestLinkAPI api,TestPlan plan) {
		HashMap<String, TestCase[]> map = new HashMap<String, TestCase[]>();
		TestCase[] cases = null;
		TestSuite[] suites = null;
		suites = getTestSuites(api, plan);
		if(suites!=null) {
			for(int i=0;i<suites.length;i++) {
				cases = api.getTestCasesForTestSuite(suites[i].getId(), true, null);
				if(cases!=null) {
					map.put(suites[i].getName(), cases);					
				}
				
			}	
		}
					
		return map;
	}
	
	
	
	/**
	 * 获取指定的测试计划下的指定测试套件下的测试用例集
	 */
	public static TestCase[] getTestCasesBySuiteName(TestLinkAPI api,TestPlan plan,String suiteName) {		
		TestCase[] cases = null;
		HashMap<String,TestCase[]> map=getTestCases(api,plan);
		for(String name_str : map.keySet()) {
			cases = map.get(name_str);			
			if(name_str.equals(suiteName)) {
				break;
			}			
		}					
		return cases;
	}
	
		
	/**
	 * 根据测试用例获取测试用例的测试步骤
	 */
	public static String getStepByCase(TestLinkAPI api,TestCase testCase) {		
		List<TestCaseStep> steps = null;		
		String caseStep = null;
		TestCase tc = api.getTestCaseByExternalId(testCase.getFullExternalId(), null);
		steps = tc.getSteps();
		for(int j=0;j<steps.size();j++) {				
			TestCaseStep testCaseStep = (TestCaseStep)steps.get(j);
			caseStep = testCaseStep.getActions();
			caseStep = getRightStr(caseStep);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+caseStep);
			break;
		}
			return caseStep;
   }		
	
	/**
	 * 根据测试用例获取期望结果
	 */
	public static String getExpectResultByCase(TestLinkAPI api,TestCase testCase) {		
		List<TestCaseStep> steps = null;		
		String expectResult = null;
		TestCase tc = api.getTestCaseByExternalId(testCase.getFullExternalId(), null);
		steps = tc.getSteps();
		for(int j=0;j<steps.size();j++) {				
			TestCaseStep testCaseStep = (TestCaseStep)steps.get(j);
			expectResult = testCaseStep.getExpectedResults();
			expectResult = getRightStr(expectResult);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<"+expectResult);
			break;
		}
			return expectResult;
   }
	/**
	 * 处理字符串（操作步骤和期望结果）
	 */
    public static String getRightStr(String str) {
    	// 过滤掉html标签
		str = str.replaceAll("<([^>]*)>", "");
		//替换成正常显示的双引号
		str = StringEscapeUtils.unescapeHtml4(str);
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");				
		Matcher m = p.matcher(str);
		str = m.replaceAll("");
    	return str;
    }
    /**
	 * 获取json各个属性
	 */
    public static String getJsonStrAttr(String str,String attr) {
    	String json_attr = null;
    	JSONObject obj = new JSONObject(str);
    	json_attr = obj.getString(attr);
    	return json_attr;
    }
    
    public static JSONObject getJsonAttr(String str,String attr) {
    	JSONObject json_attr = null;
    	JSONObject obj = new JSONObject(str);
    	json_attr = obj.getJSONObject(attr);
    	return json_attr;
    }
    /**
	 * 将结果回写到testlink
	 */
    public static void reportResult(String TestProject,String TestPlan,String Testcase,String Build,String Notes,String Result) throws TestLinkAPIException{
    	String url = ResourceUtil.getKey("testlink_url");
	    String devKey = ResourceUtil.getKey("testlink_key");
    	TestLinkAPIClient api=new TestLinkAPIClient(devKey, url);    	
    	api.reportTestCaseResult(TestProject, TestPlan, Testcase, Build, Notes, Result);    	
    	}
}
