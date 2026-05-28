package StepDefinitions;

import Pojo.NewBookingRequest;
import Pojo.AdminTokenRequest;
import io.restassured.response.Response;

public class AdminContext {

    private Response response;
    private AdminTokenRequest adminTokenRequest;
    private String adminToken;
    private String bookingId;

    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }

    public AdminTokenRequest getAdminTokenRequest() {
        return adminTokenRequest;
    }

    public void setAdminTokenRequest(AdminTokenRequest adminTokenRequest) {
        this.adminTokenRequest = adminTokenRequest;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void reset() {
        adminToken = null;
        response = null;
        adminTokenRequest = null;
    }

}