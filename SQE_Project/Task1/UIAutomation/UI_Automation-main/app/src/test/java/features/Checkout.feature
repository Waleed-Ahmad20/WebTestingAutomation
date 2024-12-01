Feature: Checkout process
    As a user
    I want to complete the checkout process
    So that I can purchase the product(s)

  Scenario: Complete the checkout process
    Given the user is logged in and on products page
    When the user adds the Sauce Labs Backpack to cart
    And the user clicks on the shopping cart icon
    And the user clicks on the checkout button
    And the user enters "<firstName>" and "<lastName>" and "<postalCode>"
    And the user clicks on the continue button
    And the user clicks on the finish button
    Then the checkout process should be completed successfully

    Example:
        | firstName | lastName | postalCode |
        | Waleed    | Ahmad    | 12345      |