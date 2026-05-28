@Admin @NewBooking
Feature: Admin Module

  Background:
    Given I have booking details
      | roomid      | 101      |
      | firstname   | John     |
      | lastname    | Doe      |
      | depositpaid | true     |
      | checkin     | 2026-07-04 |
      | checkout    | 2026-07-10 |
      | email       | john.doe@example.com |
      | phone       | 123-456-7890 |
    And I send a booking request        

  Scenario: Retrieve booking details with valid admin token
    Given I have a valid admin token
    When I send a request to retrieve booking details
    Then the response should be with status code 200

  Scenario: Update booking details with valid admin token
    Given I have a valid admin token
    When I update the booking with new details
    | Description           | roomid | firstname | lastname | depositpaid | checkin    | checkout   | email                | phone        |
    | Valid booking details |    101 | John      | Doe      | true        | 2026-07-10 | 2026-07-20 | john.doe@example.com | 475-123-7890 |
    Then the response should be with status code 200
    And the details should be captured correctly

  Scenario: Delete booking with valid admin token
    Given I have a valid admin token
    When I send a request to delete the booking
    Then the response should be with status code 202

  Scenario: Update booking details for an non existing booking with valid admin token
    Given I have a valid admin token
    When I update the booking for a non existing booking with new details
    | Description           | roomid | firstname | lastname | depositpaid | checkin    | checkout   | email                | phone        |
    | Valid booking details |    101 | John      | Doe      | true        | 2026-07-10 | 2026-07-20 | john.doe@example.com | 475-123-7890 |
    Then the response should be with status code 404

  Scenario: Delete booking details for a non existing booking with valid admin token
    Given I have a valid admin token
    When I send a request to delete a non existing booking  
    Then the response should be with status code 404


