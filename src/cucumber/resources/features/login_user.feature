Feature: Login user

  Background: Login user
    Given I am a logged in user with a username "user1234"

  Scenario: User info on home site
    When I visit the "/" site
    Then I should see a "user1234" text

  Scenario: After logout redirect to the login site
    When I visit the "/" site
    And I click the "logout" button
    Then I should be on the "/login?logout" site

  Scenario: After logout I see the home site as guest user
    When I visit the "/logout" site
    And I visit the "/" site
    Then I should not see a "user1234" text