package StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import Utilities.TestConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import Pojo.RoomDetails;


public class GetRoomDetails {

    private int roomId;
    private final BookingContext bookingContext;

    public GetRoomDetails(BookingContext bookingContext) 
    {
        this.bookingContext = bookingContext;
    }

    @Given("I have a room ID {string}")
    public void i_have_a_room_id(String string) 
    {
        roomId = Integer.parseInt(string);           
    }

    @When("I send a request to get room details by ID {string}")
    public void i_send_a_request_to_get_room_details_by_id(String string) 
    {
        bookingContext.setResponse(RestAssured.given()
            .contentType("application/json")
            .log().ifValidationFails()
            .when()
            .get(String.format(TestConfig.getRoomDetailsPathTemplate(), roomId))
            .then()
            .log().ifValidationFails()
            .extract().response());
    }

    @Then("the room details should be returned with status code {string} and error message should be {string}")
    public void the_room_details_should_be_returned_with_status_code_and_error_message_should_be(String expectedResponseCode, String expectedErrorMessage) 
    {
        int expectedStatusCode = Integer.parseInt(expectedResponseCode);
        assertThat("Unexpected status code. Response: " + bookingContext.getResponse(), bookingContext.getResponse().getStatusCode(), equalTo(expectedStatusCode));
        RoomDetails response = bookingContext.getResponse().as(RoomDetails.class);
        assertThat("Invalid Response: " + bookingContext.getResponse(), response, notNullValue());
        assertThat("Invalid room details -> accessible " + response.accessible, response.accessible, notNullValue());
        assertThat("Invalid room details -> description " + response.description, response.description, notNullValue());
        assertThat("Invalid room details -> features " + response.features.toString(), response.features, hasSize(greaterThan(0)));
        assertThat("Invalid room details -> image " + response.image, response.image, notNullValue());
        assertThat("Invalid room details -> room name " + response.roomName, response.roomName, notNullValue());
        assertThat("Invalid room details -> room price " + response.roomPrice, response.roomPrice, notNullValue());
        assertThat("Invalid room details -> room ID " + response.roomid, response.roomid, notNullValue());        
        assertThat("Invalid room details -> type " + response.type, response.type, notNullValue());
    }
}
