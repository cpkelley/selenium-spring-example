package com.dealer.test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class Page implements SearchContext {
	
	@Autowired
	public SeleniumManager sm ;
	
	protected WebDriver driver ;

	protected FluentWait<WebDriver> wait ; 
	public static Logger logger ; 
	protected long defaultTimeout;
	protected long defaultTimeoutInMilliseconds ;
	
	public Page() {
		driver = sm.getDriver();
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver,15);
		logger = Logger.getLogger(this.getClass().getSimpleName());
		
	}
	
	
	public Page(SeleniumManager sm) {
		this.sm = sm;
		driver = sm.getDriver();
		PageFactory.initElements(driver, this);
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}
	
	public SeleniumManager getSm() {
		return sm;
	}


	public void setSm(SeleniumManager sm) {
		this.sm = sm;
	}

	private String timeoutErrorMessage(By by ) {
		return "Unable to find visibility of element located by " +by + " within " + defaultTimeout + " seconds \n "
				+ "\nThere several reasons why this may have occurred including: \n" +
				"\n - The page did not finish loading some ui features before the expected timeout (usually related to Ajaxian elements)" +
				"\n - The ui element is legitimately missing from the page due to a bug or feature change " +
				"\n - There was a problem with communication within the Selenium stack" +
				"\nSee image below (if available) for clues.\n If all else fails, RERUN THE TEST MANUALLY! - CPK\n\n";
	}
		
	protected WebElement waitForElement(By by, Boolean isDisplayed) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			return driver.findElement(by);
		} catch (TimeoutException te) {
			throw new TimeoutException ("Wait timed out. Unable to find " +by.toString() + ".\n " + te); 
		}
	}

	/**
	* Useful for waiting for ajaxian elements to appear. Uses the specified duration before timing out
	* Proceeds with no result if element is displayed 
	* @author ddcchrisk
	* @return nothing 
	* @throws Timeout Exception if not found
	* @param duration - the amount of time to wait in milliseconds for the element to be visible
	* @param by - the element By selector
	*/		
	protected WebElement waitForElementDisplayed(By by, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000; 
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
			tempwait.until(ExpectedConditions.visibilityOfElementLocated(by));
			//tempwait.until(new ElementPresent(by,true));
			return driver.findElement(by);
	}
	
	protected WebElement waitForElementDisplayed(final WebElement parent, final By by) {
		this.waitForElementVisible(parent);
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return parent.findElement(by).isDisplayed();
				} 
			});	
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException (timeoutErrorMessage(by) + e);
		} catch (TimeoutException e) {
			throw new TimeoutException (timeoutErrorMessage(by) + e);
		}
		return parent.findElement(by);
	}
		
	protected WebElement waitForElementPresent(final WebElement parent,	final By by) {
		try {
			wait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return parent.findElement(by);
				}
			}); 
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException (timeoutErrorMessage(by) + e);
		} catch (TimeoutException e) {
			throw new TimeoutException (timeoutErrorMessage(by) + e);
		}
		return parent.findElement(by);
	}
	
	/**
	* Useful for waiting for ajaxian elements to appear (not hidden). Uses the default timeout
	* Proceeds with no result if element is displayed 
	* @author ddcchrisk
	* @return nothing 
	* @throws Timeout Exception if not found
	* @param by - the element By selector
	*/	
	protected WebElement waitForElementDisplayed(final By by) {
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,defaultTimeout);
		try {
			tempwait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (NoSuchElementException nse) {
			throw new NoSuchElementException (timeoutErrorMessage(by) + nse);
		} catch (TimeoutException e) {
			throw new TimeoutException (timeoutErrorMessage(by) + e);
		}
		return driver.findElement(by);
	}


	/**
	* @author ddcchrisk
	* Checks to see if an element exists. Does not care if element is displayed or hidden
	* @return true if the element exists
	* @param by - the element By selector
	*/		
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		}	catch (NoSuchElementException nse) {
				logger.debug("Element '"+ by.toString() + "' not found. Returning false");
				return false;
			}
	}

	/**
	* Waits to see if an element exists. Does care if element is displayed or hidden
	* It will wait until the specified duration and then give ups
	* @author ddcchrisk
	* @return true if the element exists
	* @param by - the element By selector
	* @param duration - time to wait for the element to appear in milliseconds
	*/		
	public boolean isElementPresent(By by, long durationInMilliseconds) {
		boolean result = false;
		try {
			new WebDriverWait(driver, durationInMilliseconds/1000).until(ExpectedConditions.presenceOfElementLocated(by));
			result =  true;
		} catch (NoSuchElementException nse) {
			logger.debug("Element '"+ by.toString() + "' not found. Returning false");
		}
		return result;
	}
	
	/**
	* Checks to see if an element exists and is displayed (visible) on the page
	* @author ddcchrisk
	* @return true if the element is displayed
	* @param by - the element By selector
	*/	
	public boolean isElementPresentAndDisplayed(By by) {
		if (isElementPresent(by)) {
		   return driver.findElement(by).isDisplayed();
		} else {
			logger.debug("Unable to find element: " +by.toString());
		   return false;
		}
	}
	
	/**
	* Checks to see if an element below a parent element exists and is displayed (visible) on the page
	* @author ddcchrisk
	* @return true if the element is displayed
	* @param by - the element By selector
	*/
	public boolean isElementPresentAndDisplayed(WebElement parent,
			By by) {
		try {
			return parent.findElement(by).isDisplayed();
		}	catch (NoSuchElementException nse) {
				logger.debug("Element '"+ by.toString() + "' not found. Returning false");
				return false;
			}

	}

	/**
	 * 
	 * Useful for waiting for ajaxian elements to appear and returns the result 
	* This will check if the element is present and wait for it to display. 
	* @author ddcchrisk
	* @return true if element exists and displayed within the duration 
	* @param duration - the amount of time to wait in milliseconds for the element to be visible
	* @param by - the element By selector
	*/	
	public boolean isElementPresentAndDisplayed(By by, long durationInMilliseconds) {
		boolean result = false;
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
		//.ignoring(NoSuchElementException.class);
		try {
			tempwait.until(ExpectedConditions.visibilityOfElementLocated(by));
			result = true;
		} catch (TimeoutException te) {
			logger.debug("Timed out. Unable to find element: " +by.toString());
			result = false;
		} 

		return result;

	}
	
	protected boolean isElementPresentAndDisplayed(final WebElement parent,
			final By by, long durationInMilliseconds) {
		boolean result = false;
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
		//.ignoring(NoSuchElementException.class);
		try {
			tempwait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return parent.findElement(by);
				}
			}); 
			result = true;
		} catch (TimeoutException te) {
			logger.debug("Timed out. Unable to find element: " +by.toString());
			result = false;
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException ("Unable to find visibility of element located by " +by  + " within " + this.defaultTimeout + " seconds \n " + e);
		}
		return result; 

	}
	
	/**
	 * Useful for waiting for ajaxian elements to disappear from the page(not just be hidden) 
	 * Does not verify if the element is displayed or not.
	 * @author ddcchrisk
	 * @return the WebElement if it appears
	 * @throws Assertion error if element is not removed from DOM within duration specified   
	 * @param duration - the amount of time to wait in milliseconds for the element to disappear before error is thrown
	 * @param by - the element By selector
	 */
	public void waitforElementToDisappear(By by, long duration) {
		if (this.isElementPresent(by)) {
			StopWatch timer = new StopWatch();
			timer.start();
			while(driver.findElements(by).size() != 0) {
				
				if (timer.getTime() > duration) {
					timer.stop();
					Assert.fail("\nTimed out waiting for locator '"+by.toString()
							+ "' to disappear within " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
				}
			}	
		}
		
	}
	
	public void waitforElementToDisappear(By by) {
		logger.info("Default Timeout: " + defaultTimeout*1000);
		waitforElementToDisappear(by, defaultTimeout*1000);
		
	}
	
	
	
	/**
	 * Useful for waiting for  elements to Hide from the page(not just be hidden) 
	 * Does not verify if the element is displayed or not.
	 * @author ddcchrisk
	 * @throws Assertion error if element is not removed from DOM within duration specified   
	 * @param duration - the amount of time to wait in milliseconds for the element to disappear before error is thrown
	 * @param by - the element By selector
	 */
	public void waitforElementToHide(By by, long duration) {
		boolean result = true;
		if (this.isElementPresent(by)) {
			StopWatch timer = new StopWatch();
			timer.start();
			while(result == true) {
				result = driver.findElement(by).isDisplayed();
				
				if (timer.getTime() > duration) {
					timer.stop();
					Assert.fail("\nTimed out waiting for locator '"+by.toString()
							+ "' to disappear within " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
				}
			}	
		}
		
	}

	public void waitForElementNotVisible(By by, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
		tempwait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}
	
	public void waitForElementNotVisible(WebElement element, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
		tempwait.until(ExpectedConditions.stalenessOf(element));
	}

	public void clickPage(String pageName) {
		driver.findElement(By.xpath("//a[contains(.,'"+pageName+"')]")).click();
		
	}
	
	public void setValue(WebElement field, String value) {		
		field.clear();
		field.sendKeys(value);	
	}
	
	public void setSelectedValue(Select field, String value) {
		field.selectByValue(value);
	}
	
	public String getBrowserTitle() {
		return driver.getTitle();
	}
	
	
	/**
	 * @author ddcchrisk
	 * Modifies the implicit wait time for an element. Useful for waiting for ajaxian elements. 
	 * Does not verify if the element is displayed or not. But you can then use isDisplayed() in conjuction. 
	 * @args wait - the amount of time to wait in milliseconds for the element to display
	 */
	public WebDriver implicityWait(WebDriver tempDriver, long wait) {
		logger.warn("Switching Implicit Wait to " + wait);
		tempDriver.manage().timeouts().implicitlyWait(wait, TimeUnit.MILLISECONDS);
		return tempDriver;	
	}
	
	public void hover(By by) {
		hover(driver.findElement(by));
	}
	
	public void hover(WebElement element) {
		new Actions(driver).moveToElement(element).perform();
	}
	
	public void controlClick(WebElement element) {
		
		Actions builder = new Actions(driver);
	
		builder.keyDown(Keys.CONTROL);
		builder.click(element);
		builder.perform();
		builder.keyUp(Keys.CONTROL);
		builder.perform();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
	public String switchToTab() {
		Set<String> windows = driver.getWindowHandles();
		logger.info(driver.getWindowHandles().toString());
		String windowHandle = windows.toArray()[(windows.size()-1)].toString();
		logger.info("Switching to new tab: " + windowHandle);
		driver.switchTo().window(windowHandle);
		return windowHandle;
	}
	
	public void setSelectOption(WebElement field, String option) {
		new Select(field).selectByValue(option);
	}
	
	public WebElement waitForElementVisible(WebElement element) {
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
		return element;
	}
	
	
	public WebElement watchForStaleElement(By by){

		WebElement slipperyElement = null;
		
		int count = 0; 
		while (count < 3){
		    try {
		       slipperyElement= this.waitForElementDisplayed(by, 1000);
		       break;
		     } catch (StaleElementReferenceException e){
		       //e.toString();
		       logger.info("Trying to recover from a stale element :" + e.getMessage());
		       count++ ;
		     } catch (TimeoutException te) {
		    	 count++;
		     }
		}
		return slipperyElement;
		
	}
	
	public WebElement slipperyElement(By by) {
		WebElement slippery = null;
		try {
			slippery =  this.watchForStaleElement(by);
		} catch (StaleElementReferenceException se) {
			slipperyElement(by);
		} 
		return slippery;	
	}
	
	public boolean alertExists() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException na){
			return false;
		}
	}
	
	public WebElement findElementViaText(By locator, String elementText) {
		WebElement result = null;
		this.waitForElementDisplayed(locator);
		for (WebElement button : findElements(locator)) {
			if (button.getText().equals(elementText)) {
				result = button;
				break;
			}
		}
		
		if (result == null) {
			throw new NoSuchElementException("Unable to find element with text: " +elementText + " using locator: " + locator);
		}
		
		return result;
	}
	
	public WebElement waitForVisibleElementViaText(By locator, String elementText) {
		WebElement result;
		this.waitForElement(locator,false); 
		result = waitForVisibleElementViaText(findElements(locator), elementText);

		if (result == null) {
			throw new NoSuchElementException("Unable to find element with text: '" +elementText + "' using locator: " +locator + " within " + defaultTimeout + " seconds");
		}
		
		return this.waitForElementVisible(result);

	}
	
	public WebElement waitForVisibleElementViaText(WebElement parent, By locator, String elementText) {
		WebElement result;
		this.waitForElement(locator,false); 
		result = waitForVisibleElementViaText(parent.findElements(locator), elementText);

		if (result == null) {
			throw new NoSuchElementException("Unable to find element with text: '" +elementText + "' using locator: " +locator + " within " + defaultTimeout + " seconds");
		}
		
		return this.waitForElementVisible(result);

	}
	
	private WebElement waitForVisibleElementViaText(List<WebElement> elements, String elementText) {
		WebElement result = null;
		StopWatch timer  = new StopWatch();
		timer.start();
		while (timer.getTime() < defaultTimeoutInMilliseconds && result == null) {
			for (WebElement element : elements) {
				String displayedText = element.getText(); 
				if (displayedText.equals(elementText)) {
					result = element;
					break;
				}
			}	
		}
		
		return result; 
	}
	
	public WebElement getFirstVisibleElement(By locator) {
		WebElement visible = null ;
		for (WebElement element : driver.findElements(locator)) {
			if (element.isDisplayed()) {
				visible = element;
				break;
			} 
		}
		
		if (visible == null) 
			throw new NoSuchElementException("Unable to find any visible element using locator: " + locator);
		
		return visible;
	}

	public WebElement findElementViaPartialText(By locator, String elementText) {
		WebElement result = null;
		this.waitForElementDisplayed(locator);
		for (WebElement button : findElements(locator)) {
			if (button.getText().contains(elementText)) {
				result = button;
			}
		}
		
		if (result == null) {
			throw new NoSuchElementException("Unable to find element with text: " +elementText + " using locator: " + locator);
		}
		
		return result;
	}
	
	public WebElement waitForDOM(By locator) {
		findElement(By.cssSelector("body"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//ddcchrisk - I dont' think this js script will work
		js.executeScript("_.defer(function() { $('body').append(\"<div id='#{"+locator.toString()+"}'></div>\");});");
		return findElement(locator);
	}

}
