package Pojo;

public class NewBookingRequest {
    public int roomid;
    public String firstname;
    public String lastname;
    public Boolean depositpaid;
    public BookingDates bookingdates;
    public String email;
    public String phone;

    public int getRoomId() {
        return roomid;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public Boolean isDepositPaid() {
        return depositpaid;
    }

    public BookingDates getBookingDates() {
        return bookingdates;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
