@GetRoomById
Feature: Get Room Details

  Scenario Outline: Test get room details by ID functionality with various input combinations
    Given I have a room ID "<room_id>"
    When I send a request to get room details by ID "<room_id>"
    Then the room details should be returned with status code "<expected_response_code>" and error message should be "<error_message>"

    Examples:
      | Description          | room_id | expected_response_code | error_message                   |
      | Valid room ID        |       1 |                    200 | null                            |
      | Non-existent room ID |     999 |                    404 | Room not found                  | 
