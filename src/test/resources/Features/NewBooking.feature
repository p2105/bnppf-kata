@NewBooking
Feature: New Booking

  Scenario Outline: Negative Tests for new booking functionality with various input combinations
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
    Then the response status code should be "<expected_response_code>" and error message should be "<error_message>"

    Examples:
      | Description               | roomid | firstname | lastname | depositpaid | checkin    | checkout   | email                | phone        | expected_response_code | error_message                       |
      | Missing firstname         |    102 |           | Doe      | true        | 2026-07-02 | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Firstname should not be blank       |
      | Missing lastname          |    102 | John      |          | true        | 2026-07-02 | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Lastname should not be blank        |
      | Missing deposit paid      |    102 | John      | Doe      |             | 2026-07-02 | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Failed to create booking            |
      | Missing checkin           |    102 | John      | Doe      | true        |            | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | must not be null                    |
      | Missing checkout          |    102 | John      | Doe      | true        | 2026-07-02 |            | john.doe@example.com | 123-456-7890 |                    400 | must not be null                    |
      | Missing email             |    102 | John      | Doe      | true        | 2026-07-02 | 2026-07-10 |                      | 123-456-7890 |                    400 | Failed to create booking            |
      | Missing phone             |    102 | John      | Doe      | true        | 2026-07-02 | 2026-07-10 | john.doe@example.com |              |                    400 | Failed to create booking            |
      | Invalid email format      |    102 | John      | Doe      | true        | 2026-07-02 | 2026-07-10 | john.doeexample.com  | 123-456-7890 |                    400 | must be a well-formed email address |
      | Invalid phone format      |    102 | John      | Doe      | true        | 2026-07-02 | 2026-07-10 | john.doe@example.com |       123asd |                    400 | size must be between 11 and 21      |
      | Invalid checkin dates     |    102 | John      | Doe      | asdaf       | absncs     | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Failed to create booking            |
      | Invalid checkout dates    |    102 | John      | Doe      | Acssd       | 2026-07-02 | absncs     | john.doe@example.com | 123-456-7890 |                    400 | Failed to create booking            |
      | Invalid deposit paid      |    102 | John      | Doe      | erere       | 2026-07-02 | 2026-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Failed to create booking              |
      | Checkin dates in the past |    102 | John      | Doe      | true        | 2024-07-02 | 2024-07-10 | john.doe@example.com | 123-456-7890 |                    400 | Invalid dates                         |

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
