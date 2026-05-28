package Utilities;

import Pojo.BookingDates;
import Pojo.NewBookingRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class ReUsableModules {
//
    public static NewBookingRequest createBookingRequestFromDataTable(Map<String, String> raw)
    {
        BookingDates bookingDates = new BookingDates();
        bookingDates.checkin = CommonUtilities.blankToNull(raw.get("checkin"));
        bookingDates.checkout = CommonUtilities.blankToNull(raw.get("checkout"));

        NewBookingRequest bookingDetails = new NewBookingRequest();
        bookingDetails.roomid = Integer.parseInt(raw.get("roomid"));
        bookingDetails.firstname = CommonUtilities.blankToNull(raw.get("firstname"));
        bookingDetails.lastname = CommonUtilities.blankToNull(raw.get("lastname"));
        bookingDetails.depositpaid = CommonUtilities.parseBooleanOrNull(raw.get("depositpaid"));
        bookingDetails.bookingdates = bookingDates;
        bookingDetails.email = CommonUtilities.blankToNull(raw.get("email"));
        bookingDetails.phone = CommonUtilities.blankToNull(raw.get("phone"));

        return bookingDetails;
    }

    public static void deleteBookingById(int bookingId)
    {
        String deletePathTemplate = TestConfig.getBookingDeletePathTemplate();
        int statusCode = RestAssured.given()
            .header("Cookie", "token=" + generateAdminToken())
            .when()
            .delete(String.format(deletePathTemplate, bookingId))
            .then()
            .extract()
            .statusCode();
        System.out.println("Attempted to delete booking with ID " + bookingId + ", status code: " + statusCode);
    }

    public static String generateAdminToken()
    {
        Response response = RestAssured.given()
            .contentType("application/json")
            .body("{\"username\": \"" + TestConfig.getAdminUsername() + "\", \"password\": \"" + TestConfig.getAdminPassword() + "\"}")
            .when()
            .post(TestConfig.getAdminLoginPath())
            .then()
            .extract().response();

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getString("token");
        } else {
            System.out.println("Failed to generate admin token. Status code: " + response.getStatusCode());
            return null;
        }
    }
}