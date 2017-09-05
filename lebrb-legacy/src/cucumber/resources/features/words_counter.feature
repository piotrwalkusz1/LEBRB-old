Feature: Words Counter

  Background: On the Words Counter site
    Given I am a guest user
    And I visit the "/wordscounter" site

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
      | Word | Translation | Count |          |
      | home |         dom |     5 | To learn |
      | dog  |        pies |     3 | To learn |
      | run  |      biegać |     2 | To learn |
      | jump |        skok |     1 | To learn |

  Scenario: Known words in Words Counter's result's table are marked
    Given I am a logged in user with a username "user1234"
    And The user "user1234" knows "dog, cat" words
    And I visit the "/wordscounter" site
    When I upload a file with content
      """
      dog dog dog home home cat
      """
    And I click the submit button
    Then A row containing a cell with "dog" text should have "known-word" class
    And A row containing a cell with "home" text should not have "known-word" class
    And A row containing a cell with "cat" text should have "known-word" class

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

  Scenario: A guest user can choose words to learn by clicking button in last column
    When I upload a file with content
      """
      home home cat
      """
    And I click the submit button
    And I click the button in row containing cell with "home" text
    Then A row containing a cell with "home" text should have "to-learn" class
    And A row containing a cell with "cat" text should not have "to-learn" class

  @ignore
  Scenario: A guest user can download chosen words to learn by clicking "Download words" button
    When I upload a file with content
      """
      home home home cat cat dog
      """
    And I click the submit button
    And I click the button in row containing cell with "home" text
    And I click the button in row containing cell with "dog" text
    And I click the "Download words" button
    Then I should download a file with content
      """
      home; dom
      dog; pies
      """

