package com.example.demo.repositories;

import com.example.demo.models.Trip;

import java.util.List;

public interface TripsRepository {
  List<Trip> findAll();
  Trip findById(long id);
  Trip save(Trip scooter);
  boolean deleteById(long id);
  List<Trip> findByQuery(String jpqlName, String params);
}
