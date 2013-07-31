package com.dealer.test;



import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class CmsPage extends Page {

	private String page = "/index.htm";
	protected String supportedText = "Not Supported for " + this.getClass().getSimpleName();
	
	public static final By MENU_ITEM = By.cssSelector(".nav-list .nav-children a") ;
	public static final By SUB_MENU_ITEM = By.cssSelector(".nav-list .nav-children ul li a") ;

	public CmsPage(SeleniumManager sm) {
		super(sm);
	}
	
	
	public CmsPage() {
		super();
	}
	//Include any methods, variables or classes that may be access from several cms pages 
	
	public void open(String accountId) {
		String page = "http://"
			+ accountId 
			+ sm.getCms()
			+ "/index.htm";
		
		logger.info("Opening: " +page) ;
		driver.get(page);

		
	}
	
	public void openPage(String accountId, String pageURL) {
		String page = "http://"+ accountId 
			+ sm.getCms()
			+ pageURL;
		 
		logger.info("Opening: "+page) ;
		driver.get(page);
		
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	
    public List<WebElement> getMenuItems() {
        return driver.findElements(MENU_ITEM);
	}
	
	public List<WebElement> getSubMenuItems() {
		return driver.findElements(SUB_MENU_ITEM);
	}
		
	public String formatPrice(String price) {
		String p = price.replaceAll("[^0-9]", "");
		logger.debug("Formatted Price: "+p);
		return p ;
	}

	public void waitForSubMenuItems() {
		this.isElementPresent(SUB_MENU_ITEM, 3000);
	}
	

}

