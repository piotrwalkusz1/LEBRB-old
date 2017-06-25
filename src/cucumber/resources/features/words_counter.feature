Feature: Words Counter

  Background: On the Words Counter site
    Given I visit the "/wordscounter" site

  Scenario: Click the submit button without uploading a file
    When I click the submit button
    Then I should be on the "/wordscounter" site
    And I should see a "Choose a file" text

  Scenario: If a file is valid, show a table and cart
    When I upload a valid file
    And I click the submit button
    Then I should be on the "/wordscounter/result" site
    And I should see a table
    And I should see a chart

  Scenario: If a file is too large, show an error message
    When I upload a file with size 10485761 bytes
    And I click the submit button
    Then I should be on the "/wordscounter" site
    Then I should see a "The file is too large" text

  Scenario: If a file has invalid extension, show an error message
    When I upload a file with invalid extension
    And I click the submit button
    Then I should be on the "/wordscounter/result" site
    Then I should see a "The file has invalid extension" text

  Scenario: Words Counter's result contains a total number of recognized words
    When I upload a file with content
    """
    dog cat home nonexistingword
    """
    And I click the submit button
    Then I should see a "Total number of words: 3" text

  Scenario: Words Counter's result contains a table with the most frequent words and their translations
    When I upload a file with content
      """
      home home jump home home home dog
      run run dog dog nonexistingword
      """
    And I click the submit button
    Then I should see a table whose body looks like
      | Word | Translation | Count |
      | home |         dom |     5 |
      | dog  |        pies |     3 |
      | run  |      biegać |     2 |
      | jump |        skok |     1 |

  Scenario: Known words in Words Counter's result's table are marked
    Given I am a logged in user with a username "user1234"
    And The user "user1234" knows "dog, cat" words
    And I visit the "/wordscounter" site
    When I upload a file with content
    """
    dog dog dog home home cat
    """
    And I click the submit button
    Then A table row with a cell with "dog" text should have "known-word" class
    And A table row with a cell with "home" text should not have "known-word" class
    And A table row with a cell with "cat" text should have "known-word" class

  Scenario: Words Counter's result contains a linear chart
    When I upload a file with content
      """
      home home home cat cat cat dog dog river city
      """
    And I click the submit button
    Then I should see a linear chart that looks like
    | Top frequent words | Coverage |
    |                  0 |        0 |
    |                  1 |      0.3 |
    |                  2 |      0.6 |
    |                  3 |      0.8 |
    |                  4 |      0.9 |
    |                  5 |        1 |
