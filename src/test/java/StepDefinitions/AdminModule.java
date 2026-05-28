package StepDefinitions;

import Utilities.ReUsableModules;
import Utilities.TestConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import Pojo.AdminTokenRequest;
import Pojo.NewBookingRequest;
import io.cucumber.datatable.DataTable;
import java.util.Map;


public class AdminModule 
{

    private AdminContext adminContext;
    private BookingContext bookingContext;
    public AdminModule(AdminContext adminContext, BookingContext bookingContext) 
    {
        this.adminContext = adminContext;
        this.bookingContext = bookingContext;
    }

    @Given("I have admin {string} and {string}")
    public void i_have_admin_and(String username, String password) {
        AdminTokenRequest adminTokenRequest = new AdminTokenRequest();        
        adminTokenRequest.username = username;
        adminTokenRequest.password = password;
        adminContext.setAdminTokenRequest(adminTokenRequest);
    }

    @Given("I have admin credentials")
    public void i_have_admin_credentials() 
    {
        assertThat("Admin username is not set in configuration", TestConfig.getAdminUsername(), not(isEmptyOrNullString()));
        assertThat("Admin password is not set in configuration", TestConfig.getAdminPassword(), not(isEmptyOrNullString()));
        AdminTokenRequest adminTokenRequest = new AdminTokenRequest();
        adminTokenRequest.username = TestConfig.getAdminUsername();
        adminTokenRequest.password = TestConfig.getAdminPassword();
        adminContext.setAdminTokenRequest(adminTokenRequest);
    }

    @Given("I have a valid admin token")
    public void i_have_a_valid_admin_token() {
        String token = ReUsableModules.generateAdminToken();
        adminContext.setAdminToken(token);
        assertThat("Failed to generate admin token", token, not(isEmptyOrNullString()));
    }

    @When("I send a login request with these credentials")
    public void i_send_a_login_request_with_these_credentials() 
    {
        Response response = RestAssured.given()
            .contentType("application/json")
            .body(adminContext.getAdminTokenRequest())
            .log().ifValidationFails()
            .when()
            .post(TestConfig.getAdminLoginPath())
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

    @Then("the response should be with status code {int}")
    public void the_response_should_be_with_status_code(Integer expectedStatusCode) 
    {
        Response response = adminContext.getResponse();
        assertThat("Unexpected status code", response.getStatusCode(), equalTo(expectedStatusCode));
    }

    @Then("the error message should indicate {string}")
    public void the_error_message_should_indicate(String expectedErrorMessage) {
        Response response = adminContext.getResponse();
        String actualErrorMessage = response.jsonPath().getString("error");
        assertThat("Unexpected error message", actualErrorMessage, equalTo(expectedErrorMessage));
    }

    @When("I send a request to retrieve booking details")
    public void I_send_a_request_to_retrieve_booking_details() 
    {
        System.out.println("Booking ID in context: " + bookingContext.getBookingId());
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Cookie", "token=" + adminContext.getAdminToken())
            .when()
            .get(String.format(TestConfig.getRetrieveBookingDetailsPath(), bookingContext.getBookingId()))
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

    @When("I update the booking with new details")
    public void i_update_the_booking_with_new_details(DataTable dataTable) 
    {
        Map<String, String> row = dataTable.asMaps(String.class, String.class).get(0);
        NewBookingRequest updatedDetails = ReUsableModules.createBookingRequestFromDataTable(row);
        bookingContext.setBookingDetails(updatedDetails); // Update booking details in context for later verification
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Cookie", "token=" + adminContext.getAdminToken())
            .log().ifValidationFails()
            .body(updatedDetails)
            .when()
            .put(String.format(TestConfig.getRetrieveBookingDetailsPath(), bookingContext.getBookingId()))
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

    @When("I update the booking for a non existing booking with new details")
    public void i_update_the_booking_for_a_non_existing_booking_with_new_details(DataTable dataTable) 
    {
        Map<String, String> row = dataTable.asMaps(String.class, String.class).get(0);
        NewBookingRequest updatedDetails = ReUsableModules.createBookingRequestFromDataTable(row);
        bookingContext.setBookingDetails(updatedDetails); // Update booking details in context for later verification
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Cookie", "token=" + adminContext.getAdminToken())
            .log().ifValidationFails()
            .body(updatedDetails)
            .when()
            .put(String.format(TestConfig.getRetrieveBookingDetailsPath(), "4545554")) // Using a very large number to ensure the booking does not exist
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

    @Then("the details should be captured correctly")
    public void the_details_should_be_captured_correctly() 
    {
        NewBookingRequest expectedDetails = bookingContext.getBookingDetails();
        Response response = adminContext.getResponse();

        assertThat("Booking details do not match: room id", response.jsonPath().getInt("booking.roomid"), equalTo(expectedDetails.getRoomId()));
        assertThat("Booking details do not match: first name", response.jsonPath().getString("booking.firstname"), equalTo(expectedDetails.getFirstName()));
        assertThat("Booking details do not match: last name", response.jsonPath().getString("booking.lastname"), equalTo(expectedDetails.getLastName()));
        assertThat("Booking details do not match: deposit paid", response.jsonPath().getBoolean("booking.depositpaid"), equalTo(expectedDetails.isDepositPaid()));
        assertThat("Booking details do not match: check-in", response.jsonPath().getString("booking.bookingdates.checkin"), equalTo(expectedDetails.getBookingDates().getCheckIn()));
        assertThat("Booking details do not match: check-out", response.jsonPath().getString("booking.bookingdates.checkout"), equalTo(expectedDetails.getBookingDates().getCheckOut()));
    }

    @When("I send a request to delete the booking")
    public void i_send_a_request_to_delete_the_booking()
    {
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Cookie", "token=" + adminContext.getAdminToken())
            .when()
            .delete(String.format(TestConfig.getDeleteBookingDetailsPath(), bookingContext.getBookingId()))
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

    @When("I send a request to delete a non existing booking")
    public void i_send_a_request_to_delete_a_non_existing_booking()
    {
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Cookie", "token=" + adminContext.getAdminToken())
            .when()
            .delete(String.format(TestConfig.getDeleteBookingDetailsPath(), "4545554")) // Using a very large number to ensure the booking does not exist
            .then()
            .log().ifValidationFails()
            .extract().response();
        adminContext.setResponse(response);
    }

     public void resetAdminContext() {
        adminContext.reset();
     }
}
