Feature: Showroom Feature
As an end user, I want to see the full lineup of models available from the franchise, 
so that I may choose the right model before searching inventory.  

Scenario: Showroom Test that should Pass
  Given I go to model lineup page
  When I go to model showroom page for "2014 Mazda CX-5 SUV"
  Then the page title should match vehicle title

 Scenario: Showroom Test that should Fail
  Given I go to model lineup page
  #TUTORIAL: the following model does not exist
  When I go to model showroom page for "2014 Mazda CX-9 SUV"
  Then the page title should match vehicle title