package com.dealer.test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
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

/**
 * Top level page object class. All other page objects should inheret from this class or a subclass
 * @author ddcchrisk
 *
 */
@Component
public class Page implements SearchContext {
	
	@Autowired
	public SeleniumManager sm ;
	
	protected WebDriver driver ;

	protected FluentWait<WebDriver> wait ; 
	public static Logger logger ; 
	protected long defaultTimeout;
	protected long defaultTimeoutInMilliseconds ;
	
	public Page() {
			
	}
	
	@PostConstruct
	public void init() {
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
	

	private String timeoutErrorMessage(By by ) {
		return "Unable to find visibility of element located by " +by + " within " + defaultTimeout + " seconds \n "
				+ "\nThere several reasons why this may have occurred including: \n" +
				"\n - The page did not finish loading some ui features before the expected timeout (usually related to Ajaxian elements)" +
				"\n - The ui element is legitimately missing from the page due to a bug or feature change " +
				"\n - There was a problem with communication within the Selenium stack" +
				"\nSee image below (if available) for clues.\n If all else fails, RERUN THE TEST MANUALLY! - CPK\n\n";
	}
		
	/**
	* waits pages elements to appear, until the specified timeout in milliseconds
	* If the element is not found and/or not displayed
	* within the specified time, a timeout exception is thrown. 
	* 
	* @author ddcchrisk
	* @throws Timeout Exception if not found
	* @param duration - the amount of time to wait in milliseconds for the element to be visible
	* @param locator - the element locator 
	*/		
	protected WebElement waitForElementDisplayed(By locator, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000; 
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
			return tempwait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			
	}
	
	/**
	* Waits for the page elements to appear as a sibling of the specified parent WebElement. 
	* If the element is not found and/or not displayed
	* within the default timeout in milliseconds, a timeout exception is thrown. 
	* 
	* @author ddcchrisk
	* @throws Timeout Exception if not found
	* @return the WebElement found on the page, and is displayed
	* @param parent  the parent element of which to find the child element
	* 	* @param locator  the element locator 
	*/
	protected WebElement waitForElementDisplayed(final WebElement parent, final By locator) {
		wait.until(ExpectedConditions.visibilityOf(parent));
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return parent.findElement(locator).isDisplayed();
				} 
			});	
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException (timeoutErrorMessage(locator) + e);
		} catch (TimeoutException e) {
			throw new TimeoutException (timeoutErrorMessage(locator) + e);
		}
		return parent.findElement(locator);
	}
		
	/**
	* Waits for the page element to appear specified by the locator. 
	* If the element is not found and/or not displayed
	* within the default timeout, a timeout exception is thrown. 
	*
	* @author ddcchrisk
	* @return the WebElement found on the page, and is displayed
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
	 * Returns true if an element exists on the page. Does not care if element is displayed or hidden
	 * 
	 * @author ddcchrisk
	 * @return true if the element exists on the page, doesn't check for visibility
	 * @param locator - the element By selector
	*/		
	public boolean isElementPresent(By locator) {
		try {
			driver.findElement(locator);
			return true;
		}	catch (NoSuchElementException nse) {
				logger.debug("Element '"+ locator.toString() + "' not found. Returning false");
				return false;
			}
	}

	/**
	* Waits to see if an element exists based on specified time. Does care if element is displayed or hidden
	*  
	* @author ddcchrisk
	* @return true if the element exists, doesn't check for visibility
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
	 * Returns true if an element exists on the page AND is displayed.
	 * If the element is present, but not displayed it will return false.
	 *  
	* @author ddcchrisk
	* @return true if the element is present and displayed
	* @param locator - the element By selector
	*/	
	public boolean isElementPresentAndDisplayed(By locator) {
		if (isElementPresent(locator)) {
		   return driver.findElement(locator).isDisplayed();
		} else {
			logger.debug("Unable to find element: " +locator.toString());
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
	
	
	/**
	 * 
	 * Useful for waiting for ajaxian elements to appear and returns the result 
	* This will check if the element is present and wait for it to display. 
	* @author ddcchrisk
	* @return true if element exists and displayed within the duration 
	* @param duration - the amount of time to wait in milliseconds for the element to be visible
	* @param by - the element By selector
	*/	
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
	* Waits for the page element to disappear from the page based on specified timeout.  
	* 
	* @author ddcchrisk
	* @throws Timeout Exception if still on page at the end of specified timeout
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
					throw new TimeoutException("\nTimed out waiting for locator '"+by.toString()
							+ "' to disappear within " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
				}
			}	
		}
		
	}
	
	/**
	* Waits for the page element to disappear from the page.  
	* 
	* @author ddcchrisk
	* @throws Timeout Exception if still on page at the end of default timeout
	* @return true if the element disappears, whether it's hidden or completely removed from the DOM
	* @param locator  the element locator 
	*/
	public void waitforElementToDisappear(By by) {
		logger.info("Default Timeout: " + defaultTimeout*1000);
		waitforElementToDisappear(by, defaultTimeout*1000);
		
	}
	
	  /**
	   * An expectation for checking that an element is either invisible or not
	   * present on the DOM, based on the specified timeout
	   *
	   * @param locator used to find the element
	   * 
	   */
	public void waitForElementNotVisible(By locator, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> wait = new WebDriverWait(driver,timeoutInSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}
	
	  /**
	   * An expectation for checking that an element is either invisible or not
	   * present on the DOM, based on the default wait timeout
	   *
	   * @param locator used to find the element
	   */
	public void waitForElementNotVisible(WebElement element, long durationInMilliseconds) {
		long timeoutInSeconds = durationInMilliseconds/1000;
		FluentWait<WebDriver> tempwait = new WebDriverWait(driver,timeoutInSeconds);
		tempwait.until(ExpectedConditions.stalenessOf(element));
	}

	/**
	   * Set the text value on the field element. Used for form inputs. 
	   * 
	   *
	   * @param field element to set the value
	   * @param text to set the field
	   */
	public void setValue(WebElement field, String value) {		
		field.clear();
		field.sendKeys(value);	
	}
	
	/**
	   * need i say more? 
	   * 
	   *
	   */	
	public String getBrowserTitle() {
		return driver.getTitle();
	}
	
	/**
	   * hover on an element based on the locator 
	   * 
	   *
	   */
	public void hover(By by) {
		hover(driver.findElement(by));
	}
	
	/**
	   * hover on the specified element 
	   * 
	   *
	   */
	public void hover(WebElement element) {
		new Actions(driver).moveToElement(element).perform();
	}
	
	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
	/**
	   * select the option based on option value, not text 
	   * 
	   *
	   */
	public void setSelectOption(WebElement field, String option) {
		new Select(field).selectByValue(option);
	}	

}
