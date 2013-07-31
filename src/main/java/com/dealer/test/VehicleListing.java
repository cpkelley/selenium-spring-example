package com.dealer.test;

import org.openqa.selenium.WebElement;


public interface VehicleListing {

	public abstract String getPrice();

	public abstract String getTitle();

	public abstract WebElement getLink();
	
	public abstract void click();

	public abstract boolean isCertified();	

}