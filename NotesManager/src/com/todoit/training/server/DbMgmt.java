package com.todoit.training.server;


import com.todoit.training.client.Message;
import com.todoit.training.client.Project;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class DbMgmt {
  
  Connection conn;
  
  public DbMgmt (Properties prop) {
    conn = null;
    try {
      conn = DriverManager.getConnection (prop.getProperty("url"), prop.getProperty("userName"), prop.getProperty("password"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public ArrayList<Message> getMessages () {
    String query = "select n.noteID, n.note, n.project_id, p.name from dbo.notes n " + 
      "join dbo.project p on n.project_id = p.id";
    Statement stmt = null;
    ResultSet rs = null;
    
    ArrayList<Message> result = new ArrayList<Message> ();
    try {     
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        Message message =  new Message (rs.getInt("NoteID"), rs.getString("NOTE"), rs.getInt("project_id"), rs.getString("name")); 
        result.add(message);        
      }      
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {rs.close();} catch (Exception e) {e.printStackTrace();}
      try {stmt.close();} catch (Exception e) {e.printStackTrace();}
      try {conn.close();} catch (Exception e) {e.printStackTrace();}
    }
  }
  
  public ArrayList<Project> getProjects () {
    String query = "select ID, name from dbo.project order by ID";
    Statement stmt = null;
    ResultSet rs = null;
    
    ArrayList<Project> result = new ArrayList<Project> ();
    try {     
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        Project project =  new Project (rs.getInt("ID"), rs.getString("name")); 
        result.add(project);        
      }      
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {rs.close();} catch (Exception e) {e.printStackTrace();}
      try {stmt.close();} catch (Exception e) {e.printStackTrace();}
      try {conn.close();} catch (Exception e) {e.printStackTrace();}
    }
  }
  
  public void updateMessage (int noteID, String updatedNote) {    
    String query = "update dbo.notes set note = ? where noteID = ?";
    PreparedStatement stmt = null;    
    try {
      stmt = conn.prepareStatement (query);
      stmt.setString (1, updatedNote);
      stmt.setInt (2, noteID);      
      stmt.executeUpdate();            
    } catch (Exception e) {
      e.printStackTrace();      
    } finally {
      try {stmt.close();} catch (Exception e) {e.printStackTrace();}
      try {conn.close();} catch (Exception e) {e.printStackTrace();}
    }
  }
  
  public void removeMessage (int noteID) {
    String query = "delete from dbo.notes where noteID = ?";
    PreparedStatement stmt = null;
    try {     
      stmt = conn.prepareStatement (query);
      stmt.setInt (1, noteID);
      stmt.executeUpdate();            
    } catch (Exception e) {
      e.printStackTrace();      
    } finally {
      try {stmt.close();} catch (Exception e) {e.printStackTrace();}
      try {conn.close();} catch (Exception e) {e.printStackTrace();}
    }
  }
  
  public int insertMessage (String newNote, int projectID) {
    String[] returnId = { "NoteID" };
    String query = "insert into dbo.notes (note, project_id) values (?, ?)";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int result = 0;
    try { 
      stmt = conn.prepareStatement(query, returnId);
      stmt.setString (1, newNote);
      stmt.setInt(2, projectID);
      stmt.executeUpdate();     
      rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        result = rs.getInt(1);           
      }
      rs.close();
      return result;      
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    } finally {
      try {rs.close();} catch (Exception e) {e.printStackTrace();}
      try {stmt.close();} catch (Exception e) {e.printStackTrace();}
      try {conn.close();} catch (Exception e) {e.printStackTrace();}
    }    
  }
  
}



