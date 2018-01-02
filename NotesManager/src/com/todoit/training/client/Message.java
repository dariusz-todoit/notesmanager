package com.todoit.training.client;

import java.io.Serializable;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private int id;
  private String note;

  public Message(){};
  
  public Message (int id, String note) {
    this.id = id;
    this.note = note;
  }

  public String getNote () {
    return note;
  }
  
  public int getMessageID () {
    return id;
  }
}
