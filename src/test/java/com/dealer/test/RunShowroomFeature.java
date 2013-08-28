package com.dealer.test;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cuke-reports"},
		features = "classpath:showroom.feature")
public class RunShowroomFeature {

}
