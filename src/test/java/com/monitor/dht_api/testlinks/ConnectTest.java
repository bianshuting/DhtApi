package com.monitor.dht_api.testlinks;


import org.testng.annotations.Test;
import com.monitor.dht_api.handler.TestlinkHandler;


public class ConnectTest {	
	@Test
	public void connectTestlink() {
	    TestlinkHandler.testlinkExecute();	    
	}

}
