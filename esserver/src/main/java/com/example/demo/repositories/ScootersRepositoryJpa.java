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
public class ScootersRepositoryJpa implements ScootersRepository {

  @Autowired
  private EntityManager em;

  @Override
  public List<Scooter> findAll() {
    TypedQuery<Scooter> query = this.em.createQuery("select s from Scooter s", Scooter.class);
    return query.getResultList();
  }

  @Override
  public Scooter findById(long id) {
    return em.find(Scooter.class, id);
  }

  @Override
  public Scooter save(Scooter scooter) {
    return em.merge(scooter);
  }

  @Override
  public boolean deleteById(long id) {
    Scooter scooter = findById(id);
    Scooter toRemove = em.merge(scooter);
    em.remove(toRemove);
    return true;
  }

  @Override
  public List<Scooter> findByQuery(String jpqlName, String params) {
    if(jpqlName == "Scooter_find_by_battery"){
      TypedQuery<Scooter> namedQuery =
        em.createNamedQuery(jpqlName, Scooter.class);

      return namedQuery
        .setParameter("max", Integer.parseInt(params))
        .getResultList();
    }
    else{
      TypedQuery<Scooter> namedQuery =
        em.createNamedQuery(jpqlName, Scooter.class);

      return namedQuery
        .setParameter("status", Scooter.getScooterStatusViaString(params))
        .getResultList();
    }
  }
}
