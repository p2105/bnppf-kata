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

}
