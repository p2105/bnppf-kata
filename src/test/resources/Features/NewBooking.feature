@NewBooking
Feature: New Booking
  Scenario Outline: Positive Test for new booking functionality with various input combinations
    Given I have booking details
      | roomid      | <roomid>      |
      | firstname   | <firstname>   |
      | lastname    | <lastname>    |
      | depositpaid | <depositpaid> |
      | checkin     | <checkin>     |
      | checkout    | <checkout>    |
      | email       | <email>       |
      | phone       | <phone>       |
    When I send a booking request
    Then booking should be created successfully with status code "<expected_response_code>"
    And the user details should be captured correctly in the response

    Examples:
      | Description           | roomid | firstname | lastname | depositpaid | checkin    | checkout   | email                | phone        | expected_response_code |
      | Valid booking details |    101 | John      | Doe      | true        | 2026-07-04 | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    201 |
