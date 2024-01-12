package com.example.demo.repositories;

import com.example.demo.models.Scooter;
import com.example.demo.rest.ScootersController;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

public class ScootersRepositoryMock implements ScootersRepository {
    private List<Scooter> scooters = new ArrayList<>();
    private static long i = 30001;

    public ScootersRepositoryMock() {
        for (int j = 0; j < 7; j++) {
            scooters.add(Scooter.createRandomScooter());
            Scooter scooter = scooters.get(j);
            scooter.setId(i++);
        }
    }

    @Override
    public List<Scooter> findAll() {
        return scooters;
    }

    @Override
    public Scooter findById(long id) {
      for(Scooter scooter: scooters){
        if(scooter.getId() == id){
          return scooter;
        }
      }
      return null;
    }

    @Override
    public Scooter save(Scooter scooter) {
      if(scooter.getId() == 0){
        scooter.setId(i++);
        scooters.add(scooter);
        return scooter;
      }
      else{
        for (int j = 0; j < this.scooters.size(); j++) {
          if(this.scooters.get(j).getId() == scooter.getId()){
            scooters.set(j, scooter);
          }
        }

        return scooter;
      }
    }

    @Override
    public boolean deleteById(long id) {
      Scooter scooter = findById(id);
      if(scooter != null){
        scooters.remove(scooter);
        return true;
      }
      return false;
    }

  @Override
  public List<Scooter> findByQuery(String jpqlName, String params) {
    return null;
  }
}
