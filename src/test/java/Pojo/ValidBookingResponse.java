package Pojo;

public class ValidBookingResponse 
{
    public BookingDates bookingdates;
    public int bookingid;
    public boolean depositpaid;
    public String firstname;
    public String lastname;
    public int roomid;

    public int getBookingid() {
        return bookingid;
    }

    public BookingDates getBookingDates() {
        return bookingdates;
    }

    public boolean isDepositPaid() {
        return depositpaid;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public int getRoomId() {
        return roomid;
    }

    public NewBookingRequest getBooking() {
        NewBookingRequest booking = new NewBookingRequest();
        booking.roomid = this.roomid;
        booking.firstname = this.firstname;
        booking.lastname = this.lastname;
        booking.depositpaid = this.depositpaid;
        booking.bookingdates = this.bookingdates;
        return booking;
    }
}