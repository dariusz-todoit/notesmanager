package com.todoit.training.server;


import com.todoit.training.client.Message;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// import java.sql.SQLException;
import java.sql.Statement;
// import com.todoit.training.server.ReadFromProperties;
import com.todoit.training.server.Config;
// import java.util.Properties;

public class DbMgmt {
  
  Connection conn;
  
  public DbMgmt () {
    conn = null;
    // ReadFromProperties readFromProperties = new ReadFromProperties ();
    Config config = new Config ();     
    // Properties prop = readFromProperties.prop;
    
    // String url = "jdbc:jtds:sqlserver://127.0.0.1;instance=SQLEXPRESS;DatabaseName=notes";
    // String userName = "sa";
    // String password = "Dominik";
    try {
      conn = DriverManager.getConnection (config.getProperty("url"), 
        config.getProperty("userName"), config.getProperty("password"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public ArrayList<Message> getMessages () {
    String query = "select noteID, note from dbo.notes";
    Statement stmt = null;
    ResultSet rs = null;
    
    ArrayList<Message> result = new ArrayList<Message> ();
    try {     
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        Message message =  new Message (rs.getInt("NoteID"), rs.getString("NOTE")); 
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
  
  public int insertMessage (String newNote) {
    String[] returnId = { "NoteID" };
    String query = "insert into dbo.notes (note) values (?)";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int result = 0;
    try { 
      stmt = conn.prepareStatement(query, returnId);
      stmt.setString (1, newNote);
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



