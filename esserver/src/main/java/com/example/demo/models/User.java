package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class User {
  @Id
  private long id;
  private String name;
  private String email;
  @JsonIgnore
  private String hashedPassWord;
  private boolean admin;

  private Random rand = new Random();


  public User(String email) {
    this.id = rand.nextInt(10000);
    this.name = email.substring(0,email.indexOf("@"));
    this.email = email;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashedPassWord() {
    return hashedPassWord;
  }

  public void setHashedPassWord(String hashedPassWord) {
    this.hashedPassWord = hashedPassWord;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", eMail='" + email + '\'' +
      ", hashedPassWord='" + hashedPassWord + '\'' +
      ", admin=" + admin +
      '}';
  }
}
