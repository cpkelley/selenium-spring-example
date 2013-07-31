package com.dealer.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dealer.test.CmsPage;
import com.dealer.test.ModelLineupPage;
import com.dealer.test.SeleniumManager;
import com.dealer.test.ShowroomDetailsPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import cucumber.api.junit.Cucumber;

public class ShowroomSteps {

	
	CmsPage cms ;
	ModelLineupPage mlp;	
	ShowroomDetailsPage sdp ;

	@Autowired
	private SeleniumManager sm;

	
	String expectedTitle; 

	@Given("^I go to model lineup page$")
	public void i_go_to_cms_page() {
		cms = new CmsPage();
		cms.openPage("ddctest0006", "/showroom/index.htm");	
	}
	
	
	@When("^I go to model showroom page for \"(.+)\"$")
	public void blee(String expectedTitle) {
		
		this.expectedTitle = expectedTitle;
		mlp = new ModelLineupPage();
		mlp.clickShowroomVehicle(expectedTitle);	
	}
	
	
	@Then("^the title should match$")
	public void blah() {
		sdp = new ShowroomDetailsPage();
		String displayedTitle =  sdp.getModelTitle();
		
		assertThat(displayedTitle, equalTo(expectedTitle));	
	}
	
}
