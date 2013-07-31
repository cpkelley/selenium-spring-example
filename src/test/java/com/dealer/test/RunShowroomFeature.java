package com.dealer.test;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:showroom.feature")
public class RunShowroomFeature {

}
