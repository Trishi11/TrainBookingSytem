package model;

public class Train {
    private int trainId;
    private String trainName;
    private String trainType;
    private int totalSeats;
    private double farePerKm;

    public Train() {}
    public Train(int id, String name, String type, int seats, double fare) {
        this.trainId = id; this.trainName = name; this.trainType = type; this.totalSeats = seats; this.farePerKm = fare;
    }

    // getters and setters
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public double getFarePerKm() { return farePerKm; }
    public void setFarePerKm(double farePerKm) { this.farePerKm = farePerKm; }
}