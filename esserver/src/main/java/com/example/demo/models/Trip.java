package com.example.demo.models;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
public class Trip {
  @TableGenerator(name = "trip_id_Gen", initialValue = 100000)
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "trip_id_Gen")
  private long id;
  private LocalDateTime start;
  private LocalDateTime end;
  private String startPosition;
  private String endPosition;
  private double mileage;
  private double cost;

  @ManyToOne(fetch = FetchType.LAZY)
  private Scooter scooter;

  @OneToOne(mappedBy = "currentTrip",cascade = CascadeType.ALL)
  private Scooter currentScooter;

  private static DecimalFormat decimalFormat4 = new DecimalFormat("#.####");

  public Trip() {
  }

  public Trip(LocalDateTime start, Scooter scooter) {
    this.start = start;
    this.startPosition = scooter.getGpsLocation();
    this.mileage = scooter.getMileage();
    if (scooter.getStatus() == ScooterStatus.IDLE){
      this.endPosition = decimalFormat4.format(52 + Math.random()) + "N, " + decimalFormat4.format(4 + Math.random()) + "E";
      this.end = this.start.plusDays(1);
      this.cost = Math.random() * 10;
    }
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public String getStartPosition() {
    return startPosition;
  }

  public void setStartPosition(String startPosition) {
    this.startPosition = startPosition;
  }

  public String getEndPosition() {
    return endPosition;
  }

  public void setEndPosition(String endPosition) {
    this.endPosition = endPosition;
  }

  public double getMileage() {
    return mileage;
  }

  public void setMileage(double mileage) {
    this.mileage = mileage;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public Scooter getScooter() {
    return scooter;
  }

  public void setScooter(Scooter scooter) {
    this.scooter = scooter;
  }

  public Scooter getCurrentScooter() {
    return currentScooter;
  }

  public void setCurrentScooter(Scooter currentScooter) {
    this.currentScooter = currentScooter;
  }

  @Override
  public String toString() {
    return "Trip{" +
      "id=" + id +
      ", start=" + start +
      ", end=" + end +
      ", startPosition='" + startPosition + '\'' +
      ", endPosition='" + endPosition + '\'' +
      ", mileage=" + mileage +
      ", cost=" + cost +
      ", scooter=" + scooter +
      '}';
  }
}
