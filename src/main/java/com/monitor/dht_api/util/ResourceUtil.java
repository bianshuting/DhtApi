package com.monitor.dht_api.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ResourceUtil {
	private static final ResourceBundle resourceBundle;

    static{
        resourceBundle = ResourceBundle.getBundle("systemConfig");
    }

    public static String getKey(String key){
       return resourceBundle.getString(key);
    }
    
    public static String getCurrentTime() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());
    }
    
}
