package com.example.demo.models;

import com.example.demo.rest.ScooterView;
import com.example.demo.rest.ScootersController;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

enum ScooterStatus {
  IDLE,
  INUSE,
  MAINTENANCE;
}

@Entity
@NamedQueries({
  @NamedQuery(name="Scooter_find_by_status",
    query="SELECT s from Scooter s WHERE s.status = :status"),
  @NamedQuery(name="Scooter_find_by_battery",
    query="SELECT s from Scooter s WHERE s.batteryCharge < :max"),
//  @NamedQuery(name="Trip_find_current_from_scooter",
//    query="SELECT t from Trip t INNER JOIN Scooter s ON s.id = t.scooter_id WHERE s.status = :status"),
})

public class Scooter {
    @JsonView(ScooterView.someScooterAttributes.class)
    @TableGenerator(name = "scooter_id_Gen", initialValue = 30000)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "scooter_id_Gen")
    private long id;
    @JsonView(ScooterView.someScooterAttributes.class)
    private String tag;
    @JsonView(ScooterView.someScooterAttributes.class)
    @Enumerated(EnumType.STRING)
    private ScooterStatus status;
    @JsonView(ScooterView.someScooterAttributes.class)
    private int batteryCharge;

    @JsonBackReference
    @OneToMany(mappedBy = "scooter",cascade = CascadeType.ALL)
    private List<Trip> trips;

    @OneToOne(fetch = FetchType.LAZY)
    private Trip currentTrip;

    private String gpsLocation;
    private double mileage;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static DecimalFormat decimalFormat4 = new DecimalFormat("#.####");

  public Scooter() {
      this.id = 300000;
      this.tag = "sssss" + randomAlphaNumeric(5);
      this.mileage = Math.floor((Math.random() * 10000) + 2);
      this.status = randomEnumValue();
      this.batteryCharge = (int) Math.floor((Math.random() * 95.0) + 5.0);
      this.gpsLocation = this.status == ScooterStatus.INUSE ? null : (decimalFormat4.format(52 + Math.random()) + "N, " + decimalFormat4.format(4 + Math.random()) + "E");

  }

  public static ScooterStatus getScooterStatusViaString(String params) {
      try{
        return ScooterStatus.valueOf(params.toUpperCase());
      }
      catch(Exception ex){
        throw new ScootersController.BadRequestException("status=" + params + " is not a valid scooter status value");
      }
  }

  private ScooterStatus randomEnumValue() {
        int pick = new Random().nextInt(ScooterStatus.values().length);
        return ScooterStatus.values()[pick];
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
          int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
          builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static Scooter createRandomScooter(){
        Scooter scooter = new Scooter();
        return scooter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(ScooterStatus status) {
        this.status = status;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getBatteryCharge() {
        return batteryCharge;
    }

    public void setBatteryCharge(int batteryCharge) {
        this.batteryCharge = batteryCharge;
    }

    public List<Trip> getTrips() {
      return trips;
    }

    public void setTrips(List<Trip> trips) {
      this.trips = trips;
    }

    public Trip getCurrentTrip() {
      return currentTrip;
    }

    public void setCurrentTrip(Trip currentTrip) {
      this.currentTrip = currentTrip;
    }

    public Trip startNewTrip(LocalDateTime startDateTime){
      Scooter scooter = this;
      Trip trip = new Trip(startDateTime, scooter);
//      if(scooter.getStatus() == ScooterStatus.IDLE){
//        trip.setStart(startDateTime);
//        trip.setStartPosition(scooter.getGpsLocation());
//        trip.setCurrentScooter(scooter);
//        scooter.setStatus(ScooterStatus.INUSE);
//        this.currentTrip = trip;
//        if(scooter.getStatus() == ScooterStatus.INUSE){
//          this.currentTrip.setStartPosition(scooter.getGpsLocation());
//          this.currentTrip.setMileage(scooter.mileage);
//        }
//        return trip;
//      }
      return trip;
    }

    public void addTrip(Trip trip){
      Scooter scooter = this;
      scooter.setStatus(ScooterStatus.INUSE);
      this.currentTrip.setStartPosition(scooter.getGpsLocation());
      this.currentTrip.setMileage(scooter.mileage);
      this.currentTrip = trip;
    }
}
