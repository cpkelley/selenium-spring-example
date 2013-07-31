package com.dealer.test;



import org.apache.log4j.Logger;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dealer.test.CmsPage;
import com.dealer.test.ModelLineupPage;
import com.dealer.test.SeleniumManager;
import com.dealer.test.ShowroomDetailsPage;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:cucumber.xml" })
public class ShowroomSpringTest {
	public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	CmsPage cms ;
	ModelLineupPage mlp;	
	ShowroomDetailsPage sdp ;
	
	@Autowired
	private SeleniumManager sm;
	
	@Test
	public void sampleTest() throws Throwable {
		
			
		String expectedTitle = "2014 Mazda CX-5 SUV";
		//Given I go to cms page x		
		cms = new CmsPage(sm);
		cms.openPage("ddctest0006", "/showroom/index.htm");
		
		//When we go to model showroom page for "2014 Mazda CX-5 SUV"
		mlp = new ModelLineupPage(sm);
		mlp.clickShowroomVehicle(expectedTitle);
		
		//Then verify title is 2014 Mazda CX-5 SUV
		sdp = new ShowroomDetailsPage(sm);
		String displayedTitle =  sdp.getModelTitle();
		
		assertThat(displayedTitle, equalTo(expectedTitle));
		
	}
	 
}
