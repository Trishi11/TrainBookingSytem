package model;

import java.sql.Date;

public class Booking {
    private int bookingId;
    private int trainId;
    private String source;
    private String destination;
    private Date dateOfJourney;
    private int seatCount;
    private double totalFare;
    private String passengerName;
    private String passengerContact;

    // getters/setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Date getDateOfJourney() { return dateOfJourney; }
    public void setDateOfJourney(Date dateOfJourney) { this.dateOfJourney = dateOfJourney; }
    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getPassengerContact() { return passengerContact; }
    public void setPassengerContact(String passengerContact) { this.passengerContact = passengerContact; }
}