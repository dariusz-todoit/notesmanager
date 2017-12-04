package com.todoit.training.client;

import java.io.Serializable;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String id;
  private String note;

  public Message(){};
  
  public Message (String id, String note) {
    this.id = id;
    this.note = note;
  }

  public String getNote () {
    return note;
  }
  
  public String getMessageID () {
    return id;
  }
}