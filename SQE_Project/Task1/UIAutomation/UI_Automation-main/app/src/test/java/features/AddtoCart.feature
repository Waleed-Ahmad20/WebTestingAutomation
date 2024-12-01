Feature: Add product(s) to cart
  As a user
  I want to add one or more products to my shopping cart
  So that I can proceed to checkout

  Scenario: Add Sauce Labs Backpack to the cart and verify cart item count
    Given the user is logged in and on the products page
    When  the user adds the Sauce Labs Backpack to the cart
    Then  the product should be successfully added to the cart
    And   the cart item count should be "1"