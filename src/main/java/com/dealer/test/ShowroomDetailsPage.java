package com.dealer.test;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;


@Component("sdp")
public class ShowroomDetailsPage extends CmsPage {

	private static final By overview_tab = By.xpath("//ul[contains(@class,'ui-tabs-nav')]/li/a[contains(.,'Overview')]");
	private static final By models_tab = By.xpath("//ul[contains(@class,'ui-tabs-nav')]/li/a[contains(.,'Models')]");
	private static final By overview = By.cssSelector("#overview");
	private static final By models = By.id("models");
	private static final By price =  By.xpath("//strong[preceding-sibling::em[contains(.,'Starting at')]]");
	private static final By fuel_economy = By.cssSelector(".fuel-efficiency");
	private static final By city_mpg = By.xpath("//div[contains(.,'City MPG')]");
	private static final By highway_mpg = By.xpath("//div[contains(.,'Hwy MPG')]");
	private static final By model_swatch = By.className("swatches");
	private static final By trim_model = By.cssSelector(".model strong");
	
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
		driver.findElement(overview_tab).click();
		waitForElementDisplayed(overview);
	}
	
	
	public void clickModelsTab() {
		driver.findElement(models_tab).click();
		waitForElementDisplayed(models);

	}
		
	public String getPrice() {
		return this.isElementPresentAndDisplayed(price) ?
				driver.findElement(price).getText() :
					null;	
	}
	
	public boolean hasFuelEconomyDisplayed() {
		 return isElementPresentAndDisplayed(fuel_economy) &&
				isElementPresentAndDisplayed(city_mpg) &&
				isElementPresentAndDisplayed(highway_mpg);
	}
	
	public boolean hasModelSwatch() {
		return isElementPresentAndDisplayed(model_swatch);
	}
	
	
	public boolean hasOverviewDisplayed() {
		return isElementPresentAndDisplayed(overview);
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
