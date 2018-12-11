package com.monitor.dht_api.handler;

import java.io.File;

import org.apache.log4j.Logger;
import org.testng.Reporter;

import com.monitor.dht_api.util.ExcelUtil;
import com.monitor.dht_api.util.ResourceUtil;

public class ExcelHandler {
	private static Logger logger = Logger.getLogger(ExcelHandler.class);
	private static String filePath = ResourceUtil.getKey("excel_path");
	private static String sheetName = ResourceUtil.getKey("excel_sheet");
	
	public static void testlinkExecute() {		
		String contentTypeStr = ExcelUtil.readExcelCell(filePath, sheetName, 0, 1);
		logger.info("contentType:"+contentTypeStr);
		Reporter.log("contentType:"+contentTypeStr);
		String methodStr = ExcelUtil.readExcelCell(filePath, sheetName, 1, 1);
		logger.info("method:"+methodStr);
		Reporter.log("method:"+methodStr);
		
		int start_line = 3;
		
		for(;start_line<8;start_line++) {
			String caseName = ExcelUtil.readExcelCell(filePath, sheetName, start_line, 1);
			logger.info("caseName:"+caseName);
			Reporter.log("caseName:"+caseName);
			String urlStr = ExcelUtil.readExcelCell(filePath, sheetName, start_line, 2);
			logger.info("请求url:"+urlStr);
			Reporter.log("请求url:"+urlStr);
			String requestStr = ExcelUtil.readExcelCell(filePath, sheetName, start_line, 3);
			String statusCodeStr = ExcelUtil.readExcelCell(filePath, sheetName, start_line, 6);		
			statusCodeStr = statusCodeStr.substring(0,statusCodeStr.indexOf("."));			
			String responseStr = DhtHandler.queryVpnServer_url(methodStr, contentTypeStr, urlStr, requestStr);
			if(responseStr!=null) {
				ExcelUtil.writeExcelCell(filePath, sheetName, start_line, 5, responseStr);
				logger.info("返回结果："+responseStr);
				Reporter.log("返回结果："+responseStr);
			}else {
				logger.info("无返回结果！");
				Reporter.log("无返回结果！");
				responseStr = "无返回结果";
				ExcelUtil.writeExcelCell(filePath, sheetName, start_line, 5, responseStr);
			}
			
			String statusCodeExpec = DhtHandler.getStatusCode(methodStr, contentTypeStr, urlStr, requestStr);
			if(statusCodeExpec!=null) {
				ExcelUtil.writeExcelCell(filePath, sheetName, start_line, 7, statusCodeExpec);
				logger.info("返回状态码："+statusCodeExpec);
				Reporter.log("返回状态码："+statusCodeExpec);
			}else {
				logger.info("请求失败！");
				Reporter.log("请求失败！");
				statusCodeExpec = "请求失败";
				ExcelUtil.writeExcelCell(filePath, sheetName, start_line, 7, statusCodeExpec);
			}			
			String testResult = DhtHandler.queryVpnServer_url(methodStr, contentTypeStr, urlStr, requestStr, statusCodeStr);
			ExcelUtil.writeExcelCell(filePath, sheetName, start_line, 8, testResult);
			logger.info("测试结果："+testResult);
			Reporter.log("测试结果："+testResult);				
			if(testResult.equals("p")) {
				ExcelUtil.setCellBackgroundColor(filePath, sheetName, start_line, 8, 1);
			}else {
				ExcelUtil.setCellBackgroundColor(filePath, sheetName, start_line, 8, 0);
			}
		}
		
	}
	

}
