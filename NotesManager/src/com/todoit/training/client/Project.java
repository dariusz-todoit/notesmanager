package com.todoit.training.client;

import java.io.Serializable;

public class Project implements Serializable {
  
  private static final long serialVersionUID = 2L;
  
  private int id;
  private String name;
  
  public Project () {};
  
  public Project (int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getID () {
    return id;
  }
  
  public String getName () {
    return name;
  }
  
}