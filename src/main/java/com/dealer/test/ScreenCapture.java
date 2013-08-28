package com.dealer.test;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sc")
public class ScreenCapture extends Page {
	File screenshot; 
	@Autowired
	SeleniumManager sm;
	
	public ScreenCapture() {
		super();
	}
	
	public File takeScreenCapture() throws IOException, InterruptedException  {
		
		//set window to common 1024x768 
		sm.getDriver().manage().window().setSize(new Dimension(1024,768));
		Thread.sleep(2000);
		
		// if using remote driver, then use augementedDriver
		if (!sm.getHost().contains("localhost")) {
			WebDriver augmentedDriver = new Augmenter().augment(sm.getDriver());
	        screenshot = ((TakesScreenshot)augmentedDriver).
	                            getScreenshotAs(OutputType.FILE); 
		} else { 
			// use the normal method of getting the screen shot
			screenshot = ((TakesScreenshot)sm.getDriver()).getScreenshotAs(OutputType.FILE); 
		}
		
		return screenshot;

		
	}

}
