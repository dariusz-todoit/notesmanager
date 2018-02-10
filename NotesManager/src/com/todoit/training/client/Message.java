package com.todoit.training.client;

import java.io.Serializable;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private int id;
  private String note;
  private int projectID;
  private String projectName;

  public Message(){};
  
  public Message (int id, String note, int projectID, String projectName) {
    this.id = id;
    this.note = note;
    this.projectID = projectID;
    this.projectName = projectName;
  }

  public String getNote () {
    return note;
  }
  
  public int getMessageID () {
    return id;
  }
  
  public int getProjectID () {
    return projectID;
  }
  
  public String getProjectName () {
    return projectName;
  }
}
