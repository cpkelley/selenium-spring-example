package com.dealer.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;


import com.dealer.test.CmsPage;
import com.dealer.test.ModelLineupPage;
import com.dealer.test.ShowroomDetailsPage;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ShowroomSteps {

	@Autowired
	CmsPage cms ;
	@Autowired
	ModelLineupPage mlp;	
	@Autowired
	ShowroomDetailsPage sdp ;
	@Autowired
	ScreenCapture sc;
	@Autowired
	SeleniumManager sm;

	String expectedTitle; 

	@Given("^I go to model lineup page$")
	public void i_go_to_cms_page() {
		//cms = new CmsPage();
		cms.openPage("ddctest0006", "/showroom/index.htm");	
	}
	
	
	@When("^I go to model showroom page for \"(.+)\"$")
	public void I_go_to_model_showroom_page_for(String expectedTitle) {
		
		this.expectedTitle = expectedTitle;
		//mlp = new ModelLineupPage();
		mlp.hasModel(expectedTitle);
		mlp.clickShowroomVehicle(expectedTitle);	
	}
	
	
	@Then("^the page title should match vehicle title$")
	public void the_title_should_match_vehicle_title() {
		//sdp = new ShowroomDetailsPage();
		String displayedTitle =  sdp.getModelTitle();
		
		assertThat(displayedTitle, equalTo(expectedTitle));	
	}
	
	
	@After
	public void takeScreenShotOnFail(Scenario result) throws Exception {
		
	      byte[] b = null;
	      FileInputStream stream = null;
		
		if (result.isFailed()) {  
			result.write("Browser: \n" +sm.getBrowser());
			
			File file = sc.takeScreenCapture();
			            
			try {
				stream = FileUtils.openInputStream(file);
			            b = IOUtils.toByteArray(stream);
			                result.embed(b, "image/png") ;
			            } finally {
			                IOUtils.closeQuietly(stream);
			            }
			            
			
		}
		
	}
	
}
