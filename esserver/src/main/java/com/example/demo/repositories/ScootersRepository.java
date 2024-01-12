package com.example.demo.repositories;

import com.example.demo.models.Scooter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface ScootersRepository {
    List<Scooter> findAll();
    Scooter findById(long id);
    Scooter save(Scooter scooter);
    boolean deleteById(long id);
    List<Scooter> findByQuery(String jpqlName, String params);
}
