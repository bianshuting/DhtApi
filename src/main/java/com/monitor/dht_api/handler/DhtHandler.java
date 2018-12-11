package com.monitor.dht_api.handler;

import static com.jayway.restassured.RestAssured.*;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;

import com.jayway.restassured.response.Response;

import testlink.api.java.client.TestLinkAPIResults;


public class DhtHandler {
	private static Logger logger = Logger.getLogger(DhtHandler.class);
	
	public static String queryVpnServer_url(String method,String contentType,String url,String requestStr,String statusCode) {		  	  
		String testResult = TestLinkAPIResults.TEST_FAILED;		
		Response res = null;
		HashMap dataObj = null;  
		  try {
			  if(method.equals("post")) {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        post(url); 
			  }else {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        get(url);
			  }			  
			  
			  if(res!=null) {
				  logger.info("测试返回内容："+res.asString());
				  Reporter.log("测试返回内容："+res.asString());				  
				  Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
				  dataObj = res.jsonPath().getJsonObject("data");
				  //Assert.assertNotNull(dataObj);
                  if(res.getStatusCode()==Integer.parseInt(statusCode) && dataObj!=null) {
					  testResult = TestLinkAPIResults.TEST_PASSED;;
				  }                  
			  }else {
				  //Assert.assertNotNull(res);
				  logger.info("请求失败，无返回内容！");
				  Reporter.log("请求失败，无返回内容！");
			  }
		  }catch(Exception e) {
			  e.printStackTrace();
			  //Assert.assertNotNull(res);
			  logger.info("请求失败，无返回内容！");
			  logger.info("失败原因："+e.getMessage());
			  Reporter.log("请求失败，无返回内容！");
			  Reporter.log("失败原因："+e.getMessage());			  
		  } 		 
		return testResult;
	  }

	public static String queryVpnServer_url(String method,String contentType,String url,String requestStr) {		  	  
		String resStr = null;		
		Response res = null;
		 
		  try {
			  if(method.equals("post")) {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        post(url); 
			  }else {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        get(url);
			  }			  
			  
			  if(res!=null) {
				  logger.info("测试返回内容："+res.asString());
				  Reporter.log("测试返回内容："+res.asString());				  
				  resStr = res.asString();
			  }else {
				  //Assert.assertNotNull(res);
				  logger.info("请求失败，无返回内容！");
				  Reporter.log("请求失败，无返回内容！");
			  }
		  }catch(Exception e) {
			  e.printStackTrace();
			  //Assert.assertNotNull(res);
			  logger.info("请求失败，无返回内容！");
			  logger.info("失败原因："+e.getMessage());
			  Reporter.log("请求失败，无返回内容！");
			  Reporter.log("失败原因："+e.getMessage());			  
		  } 		 
		return resStr;
	  }
	public static String getStatusCode(String method,String contentType,String url,String requestStr) {		  	  
		String statusCodeStr = null;		
		Response res = null;
		  
		  try {
			  if(method.equals("post")) {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        post(url); 
			  }else {
				  res = given().contentType(contentType).
					        request().body(requestStr).
					        when().
					        get(url);
			  }			  
			  
			  if(res!=null) {
				  logger.info("测试返回内容："+res.asString());
				  Reporter.log("测试返回内容："+res.asString());				  
				  statusCodeStr = String.valueOf(res.getStatusCode());
			  }
		  }catch(Exception e) {
			  e.printStackTrace();
			  //Assert.assertNotNull(res);
			  			  
		  } 		 
		return statusCodeStr;
	  }
}
