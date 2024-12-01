Feature: saucedemo.com Login

  Scenario Outline: User tries to log in with valid credentials
    Given the user is on the login page
    When the user enters "<username>" and "<password>"
    And clicks on the login button
    Then the user should be logged in successfully

    Examples:
      | username          | password    |
      | standard_user     | secret_sauce|
      | problem_user      | secret_sauce|