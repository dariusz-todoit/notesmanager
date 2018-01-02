package com.todoit.training.server;


import com.todoit.training.client.Message;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// import java.sql.SQLException;
import java.sql.Statement;

public class DbMgmt {
  
  Connection conn;

  public DbMgmt () {
    conn = null;
    String url = "jdbc:jtds:sqlserver://127.0.0.1;instance=SQLEXPRESS;DatabaseName=notes";
    String userName = "sa";
    String password = "Dominik";
    try {
      conn = DriverManager.getConnection(url, userName, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public ArrayList<Message> getMessages () {
    String query = "select noteID, note from dbo.notes";
    ArrayList<Message> result = new ArrayList<Message> ();
    try {     
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        Message message =  new Message (rs.getInt("NoteID"), rs.getString("NOTE")); 
        result.add(message);        
      }
      rs.close();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void updateMessage (int noteID, String updatedNote) {
    String query = "update dbo.notes set note = '" + updatedNote + "' where noteID = " + noteID;
    try {     
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(query);            
    } catch (Exception e) {
      e.printStackTrace();      
    } 
  }
  
  public void removeMessage (int noteID) {
    String query = "delete from dbo.notes where noteID = " + noteID;
    try {     
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(query);            
    } catch (Exception e) {
      e.printStackTrace();      
    } 
  }
  
  public int insertMessage (String newNote) {
    String[] returnId = { "NoteID" };
    String query = "insert into dbo.notes (note) values ('" + newNote + "')";
    int result = 0;
    try { 
      PreparedStatement stmt = conn.prepareStatement(query, returnId);
      stmt.executeUpdate();     
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        result = rs.getInt(1);           
      }
      rs.close();
      return result;      
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    } 
    
  }
  
  public void closeDbMgmt () {
    try {     
      conn.close();     
    } catch (Exception e) {
      e.printStackTrace();      
    }
  }
}



