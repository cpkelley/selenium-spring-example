package com.dealer.test;



import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Page Object - Abstract representation of a CMS page. All other CMS page objects should inheret from this page. 
 * Parent class is top level Page class.
 * 
 * @author ddcchrisk
 *  
 */
@Component("cms")
public class CmsPage extends Page {

	public static final By menu_item = By.cssSelector(".nav-list .nav-children a") ;
	public static final By submenu_item = By.cssSelector(".nav-list .nav-children ul li a") ;

	public CmsPage(SeleniumManager sm) {
		super(sm);
	}
	
	
	public CmsPage() {
		super();
	}
	
	/**
	 * Opens the homepage of the account specified
	 * Example: open("ddctest0001")
	 * will open http://ddctest0001.cms.qa.dealer.com/index.htm 
	 * 
	 * @param accountId
	 * @param pageUrl
	 */	
	public void open(String accountId) {
		String page = "http://"
			+ accountId 
			+ sm.getCmsHost()
			+ "/index.htm";
		
		logger.info("Opening: " +page) ;
		driver.get(page);
	
	}
	

	/**
	 * Opens the page of the account specified
	 * Example: openPage("ddctest0001", "/showroom/index.htm")
	 * will open http://ddctest0001.cms.qa.dealer.com/showroom/index.htm 
	 * 
	 * @param accountId
	 * @param pageUrl
	 */
	public void openPage(String accountId, String pageUrl) {
		String page = "http://"+ accountId 
			+ sm.getCmsHost()
			+ pageUrl;
		 
		logger.info("Opening: "+page) ;
		driver.get(page);
		
	}
	
    public List<WebElement> getMenuItems() {
        return driver.findElements(menu_item);
	}
	
	public List<WebElement> getSubMenuItems() {
		return driver.findElements(submenu_item);
	}
		
	public void waitForSubMenuItems() {
		this.isElementPresent(submenu_item, 3000);
	}
	

}

