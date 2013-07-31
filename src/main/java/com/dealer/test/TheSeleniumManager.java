package com.dealer.test;

import org.apache.log4j.Logger;


public class TheSeleniumManager {

	private volatile static SeleniumManager instance = null;
	private static Logger logger = Logger.getLogger("The Selenium Manager");
	private static boolean isSharedManager = false;
	
	private TheSeleniumManager() {
	}
	
	/*
	public static SeleniumManager getSeleniumManager() {
		if(instance == null) {
			synchronized(SeleniumManager.class){
				if(instance == null) {
					logger.info("CREATING SELENIUM MANAGER");
					//SharedManager will use the same driver session for all tests in the run
					if (isSharedManager ) {
						instance = new SharedManager();
						logger.info("Using shared manager");
					} else {
						//SeleniumManager will need to start a new session for each test scenario
						instance = new SeleniumManager();
						logger.info("Using non-shared manager");
					}
					//instance.resetProperties();
				}
		    }
		}    
		return instance;
	}
	*/

	public static void setSharedManager(boolean b) {
		TheSeleniumManager.isSharedManager = b;
	}
}
