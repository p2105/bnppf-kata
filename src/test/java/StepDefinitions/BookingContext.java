package StepDefinitions;

import Pojo.NewBookingRequest;
import Pojo.AdminTokenRequest;
import io.restassured.response.Response;

public class BookingContext {

    private NewBookingRequest bookingDetails;
    private Response response;
    

    public NewBookingRequest getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(NewBookingRequest bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void reset() {
        bookingDetails = null;
        response = null;
    }

    public String getBookingId() {
        System.out.println("Extracting booking ID from response...");
        if(response != null && response.getStatusCode() == 201) {
            System.out.println("Booking Id extracted: " + response.jsonPath().getString("bookingid"));
            return response.jsonPath().getString("bookingid");
        }
        System.out.println("Failed to extract booking ID. Response is null or status code is not 201.");
        return null;
    }

}