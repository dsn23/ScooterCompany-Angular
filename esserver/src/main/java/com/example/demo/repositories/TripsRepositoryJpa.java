package com.example.demo.repositories;

import com.example.demo.models.Scooter;
import com.example.demo.models.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class TripsRepositoryJpa implements TripsRepository  {

  @Autowired
  private EntityManager em;

  @Override
  public List<Trip> findAll() {
    TypedQuery<Trip> query = this.em.createQuery("select t from Trip t", Trip.class);
    return query.getResultList();
  }

  @Override
  public Trip findById(long id) {
    return em.find(Trip.class, id);
  }

  @Override
  public Trip save(Trip trip) {
    return em.merge(trip);
  }

  @Override
  public boolean deleteById(long id) {
    Trip trip = findById(id);
    Trip toRemove = em.merge(trip);
    em.remove(toRemove);
    return true;
  }

  @Override
  public List<Trip> findByQuery(String jpqlName, String params) {
    TypedQuery<Trip> namedQuery =
      em.createNamedQuery(jpqlName, Trip.class);

    return namedQuery
      .setParameter("status", Scooter.getScooterStatusViaString(params))
      .getResultList();
  }
}
