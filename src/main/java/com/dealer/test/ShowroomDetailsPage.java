package com.dealer.test;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;


@Component("sdp")
public class ShowroomDetailsPage extends CmsPage {

	private static final String OVERVIEW_TAB = "//ul[contains(@class,'ui-tabs-nav')]/li/a[contains(.,'Overview')]";
	private static final String MODELS_TAB = "//ul[contains(@class,'ui-tabs-nav')]/li/a[contains(.,'Models')]";
	private static final String OVERVIEW = "//div[contains(@id,'overview')]";
	private static final String MODELS = "//div[contains(@id,'models')]";
	private static final String PRICE =  "//strong[preceding-sibling::em[contains(.,'Starting at')]]";
	private static final String FUEL_ECONOMY = "//div[contains(@class,'fuel-efficiency')]";
	private static final String CITY_MPG = "//div[contains(.,'City MPG')]";
	private static final String HWY_MPG = "//div[contains(.,'Hwy MPG')]";
	private static final String MODEL_SWATCH = "//ul[contains(@class,'swatches')]";
	private By trim_model = By.cssSelector(".model strong");
	
	@FindBy (css = ".callout .inner2 h1")
	WebElement model_title;
	@FindBy (xpath = "//a[contains(@class,'ui-button') and contains(.,'View New Inventory')]")
	public WebElement viewInventory;
	@FindBy (css = "#trimsTable .trimsTableData")
	List<WebElement> trims ;
	
	public ShowroomDetailsPage() {
		super();
	}
	
	public ShowroomDetailsPage(SeleniumManager sm) {
		super(sm);
	}

	public void clickOverviewTab() {
		driver.findElement(By.xpath(OVERVIEW_TAB)).click();
		this.waitForElement(By.xpath(OVERVIEW), true);
	}
	
	
	public void clickModelsTab() {
		driver.findElement(By.xpath(MODELS_TAB)).click();
		this.waitForElement(By.xpath(MODELS), true);

	}
		
	public String getPrice() {
		return this.isElementPresentAndDisplayed(By.xpath(PRICE)) ?
				driver.findElement(By.xpath(PRICE)).getText() :
					null;	
	}
	
	public boolean hasFuelEconomyDisplayed() {
		if (isElementPresentAndDisplayed(By.xpath(FUEL_ECONOMY)) &&
				isElementPresentAndDisplayed(By.xpath(CITY_MPG)) &&
				isElementPresentAndDisplayed(By.xpath(HWY_MPG)))
				return true;
		else return false;
	}
	
	public boolean hasModelSwatch() {
		return this.isElementPresentAndDisplayed(By.xpath(MODEL_SWATCH));
	}
	
	
	public boolean hasOverviewDisplayed() {
		return this.isElementPresentAndDisplayed(By.xpath(OVERVIEW));
	}
	
	public String getModelTitle() {
		String modeltitle = model_title.getText();
		modeltitle = modeltitle.replace("\n", " ");
		return modeltitle;
	}
		
	public boolean hasModelTrimList() {
		return this.isElementPresentAndDisplayed(trim_model);
	}
			
}
