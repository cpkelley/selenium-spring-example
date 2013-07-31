Feature: Showroom Feature
I like cheese

Scenario: Test Showroom
  Given I go to model lineup page
  When I go to model showroom page for "2014 Mazda CX-5 SUV"
  Then the title should match
