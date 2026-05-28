@AdminToken
Feature: Admin Module
  
  Scenario Outline: Admin login with invalid credentials
    Given I have admin "<username>" and "<password>"
    When I send a login request with these credentials
    Then the response should be with status code <expected_response_code>
    And the error message should indicate "<error_message>"

    Examples:
      | Description                        | username | password | expected_response_code | error_message                      |
      | Invalid credsentials               | wrong    | wrong123 |                    401 | Invalid credentials                |
      | Missing username                   |          | admin123 |                    400 | Username is required               |
      | Missing password                   | admin    |          |                    400 | Password is required               |
      | Missing both username and password |          |          |                    400 | Username and password are required |
