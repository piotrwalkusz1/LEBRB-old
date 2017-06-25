Feature: Home site

  Background: Guest on the home site
    Given I am a guest user
    Given I visit the "/" site

  Scenario: Click the login button
    When I click the "login" button
    Then I should be on the "/login" site

  Scenario: Click the register button
    When I click the "register" button
    Then I should be on the "/register" site