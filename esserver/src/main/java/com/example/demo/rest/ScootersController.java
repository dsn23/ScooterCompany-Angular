package com.example.demo.rest;

import com.example.demo.models.Scooter;
import com.example.demo.models.Trip;
import com.example.demo.repositories.ScootersRepositoryJpa;
import com.example.demo.repositories.ScootersRepositoryMock;
import com.example.demo.repositories.TripsRepositoryJpa;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ScootersController extends globalConfiguration{
    @Autowired
    private ScootersRepositoryJpa scootersRepo;

    @Autowired
    private TripsRepositoryJpa tripsRepo;

    @GetMapping("/scooters")
    public List<Scooter> getAllScooters(@RequestParam(required = false) String battery, @RequestParam(required = false) String status) {
        if(battery != null && status != null){
          throw new BadRequestException("Only one parameter can be provided");
        }
        else if(battery != null){
          return this.scootersRepo.findByQuery("Scooter_find_by_battery", battery);
        }
        else if(status != null){
          return this.scootersRepo.findByQuery("Scooter_find_by_status", status);
        }
        else{
          return scootersRepo.findAll();
        }
    }

    @GetMapping("/scooters/{id}")
    public Scooter getScooter(@PathVariable long id) {
      Scooter scooter = scootersRepo.findById(id);
      if(scooter == null){
        throw new ResourceNotFoundException("Id doesnt exist in scooter list");
      }
      return scooter;
    }

    @PostMapping("/scooters")
    public ResponseEntity<Scooter> saveScooter() {
      Scooter scooter = scootersRepo.save(Scooter.createRandomScooter());
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(1).toUri();
      return ResponseEntity.created(uri).body(scooter);
    }

    @PutMapping("/scooters/{id}")
    public Scooter updateScooter(@RequestBody Scooter scooter, @PathVariable long id) {
      if(scooter.getId() != id){
        throw new PreConditionFailed("Scooter-Id=" + scooter.getId() + " does not match path parameter=" + id);
      }
      else{
        return scootersRepo.save(scooter);
      }
    }

    @DeleteMapping("/scooters/{id}")
    public boolean deleteScooter(@PathVariable long id) {
      Scooter scooter = scootersRepo.findById(id);
      if(scooter == null){
        throw new ResourceNotFoundException("Id doesnt exist in scooter list");
      }
      return scootersRepo.deleteById(id);
    }

    @PostMapping("/scooters/{scooterId}/claim")
    public Scooter claimScooter(@PathVariable long scooterId, @RequestBody Optional<LocalDateTime> starttime) {
      Scooter scooter = scootersRepo.findById(scooterId);
      if(starttime == null){
        starttime = Optional.of(LocalDateTime.now());
      }
      Trip trip = new Trip(LocalDateTime.now(), scooter);
      if(scooter.getBatteryCharge() < 10){
        throw new PreConditionFailed("Scooter-Id=" + scooter.getId() + " has a battery status lesser than 10%");
      }
      scooter.addTrip(trip);

      return scooter;
    }

    @GetMapping("/scooters/summary")
    @JsonView(ScooterView.someScooterAttributes.class)
    public List<Scooter> scooterSummary() {
      return scootersRepo.findAll();
    }

    @GetMapping("/scooters/currenttrips")
    public List<Trip> getScooterCurrentTrips() {
      return this.tripsRepo.findByQuery("Trip_find_current_from_scooter", "INUSE");
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{
      public ResourceNotFoundException(String message){
    super(message);
    }
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public class PreConditionFailed extends RuntimeException{
      public PreConditionFailed(String message){
        super(message);
      }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends RuntimeException{
      public BadRequestException(String message){
        super(message);
      }
    }
}
