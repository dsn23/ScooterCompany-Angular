package com.example.demo;

import com.example.demo.models.Scooter;
import com.example.demo.models.Trip;
import com.example.demo.repositories.ScootersRepositoryJpa;
import com.example.demo.repositories.TripsRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class EsserverApplication implements CommandLineRunner {

  @Autowired
  private ScootersRepositoryJpa scootersRepo;

  @Autowired
  private TripsRepositoryJpa tripsRepo;

	public static void main(String[] args) {
		SpringApplication.run(EsserverApplication.class, args);
	}

	@Transactional
  @Override
  public void run(String... args) throws Exception {
    System.out.println("Running CommandLine Startup");
    this.createInitialScooters();
  }

  private void createInitialScooters() {
	  List<Scooter> scooters = this.scootersRepo.findAll();
	  if(scooters != null && scooters.size() > 0) return;
    System.out.println("Configuring some initial scooter data");

    for (int i = 0; i < 7; i++) {
      Scooter scooter = Scooter.createRandomScooter();
      Trip trip = scooter.startNewTrip(LocalDateTime.now());

      Scooter savedScooter = this.scootersRepo.save(scooter);
      Trip savedTrip = this.tripsRepo.save(trip);
    }
  }
}
