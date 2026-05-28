package StepDefinitions;

import Utilities.ReUsableModules;
import Utilities.TestConfig;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import Pojo.ValidBookingResponse;
import io.cucumber.java.After;

public class NewBooking {

    private final BookingContext bookingContext;

    public NewBooking(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
    }


    @Given("I have booking details")
    public void i_have_booking_details(DataTable dataTable) 
    {
        bookingContext.setBookingDetails(
            ReUsableModules.createBookingRequestFromDataTable(dataTable.asMap(String.class, String.class))
        );
    }

    @When("I send a booking request")
    public void i_send_a_booking_request() {
        System.out.println("Booking details: " + bookingContext.getBookingDetails());
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(bookingContext.getBookingDetails())
            .log().ifValidationFails()
            .when()
            .post(TestConfig.getBookingCreatePath())
            .then()
            .log().ifValidationFails()
            .extract().response();
        bookingContext.setResponse(response);
    }

    @Then("the response status code should be {string} and error message should be {string}")
    public void the_response_status_code_should_be_and_error_message_should_be(String expectedResponseCode, String expectedErrorMessage) 
    {
        Response response = bookingContext.getResponse();
        int expectedStatusCode = Integer.parseInt(expectedResponseCode);
        assertThat("Unexpected status code.", response.getStatusCode(), equalTo(expectedStatusCode));
        
        if(!expectedErrorMessage.equals("null")) {
            assertThat("Unexpected error message.", response.jsonPath().getString("errors[0]"), equalTo(expectedErrorMessage));
        }
    }

    @Then("booking should be created successfully with status code {string}")
    public void booking_should_be_created_successfully(String expectedResponseCode) 
    {
        int actualResponseCode = bookingContext.getResponse().getStatusCode();
        int expectedStatusCode = Integer.parseInt(expectedResponseCode);
        assertThat("Unexpected status code.", actualResponseCode, equalTo(expectedStatusCode));
    }

    @Then("the user details should be captured correctly in the response")
    public void the_user_details_should_be_captured_correctly_in_the_response() 
    {
        ValidBookingResponse bookingResponse = bookingContext.getResponse().as(ValidBookingResponse.class);
        assertThat("Check-in date mismatch", bookingResponse.getBooking().getBookingDates().getCheckIn(), equalTo(bookingContext.getBookingDetails().getBookingDates().getCheckIn()));
        assertThat("Check-out date mismatch", bookingResponse.getBooking().getBookingDates().getCheckOut(), equalTo(bookingContext.getBookingDetails().getBookingDates().getCheckOut()));
        assertThat("Booking ID should be greater than 0", bookingResponse.getBookingid(), greaterThan(0));
        assertThat("Deposit paid mismatch", bookingResponse.getBooking().isDepositPaid(), equalTo(bookingContext.getBookingDetails().isDepositPaid()));
        assertThat("First name mismatch", bookingResponse.getBooking().getFirstName(), equalTo(bookingContext.getBookingDetails().getFirstName()));
        assertThat("Last name mismatch", bookingResponse.getBooking().getLastName(), equalTo(bookingContext.getBookingDetails().getLastName()));
        assertThat("Room ID mismatch", bookingResponse.getBooking().getRoomId(), equalTo(bookingContext.getBookingDetails().getRoomId()));
    }

    @After("@NewBooking")
    public void cleanUp() 
    {
        System.out.println("Cleaning up after scenario...");
        if(bookingContext.getResponse() != null && bookingContext.getResponse().getStatusCode() == 201) 
        {
            ValidBookingResponse bookingResponse = bookingContext.getResponse().as(ValidBookingResponse.class);
            int bookingId = bookingResponse.getBookingid();
            ReUsableModules.deleteBookingById(bookingId);
        }
        else 
        {
            System.out.println("No booking created, skipping cleanup.");
        }
    }
}
