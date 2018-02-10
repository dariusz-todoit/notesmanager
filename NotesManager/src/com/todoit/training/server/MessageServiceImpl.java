package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.todoit.training.client.Message;
import com.todoit.training.client.Project;
import com.todoit.training.client.MessageService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import com.todoit.training.server.DbMgmt;



public class MessageServiceImpl extends RemoteServiceServlet implements MessageService {

  private static final long serialVersionUID = 1L;  
  Properties prop = new Properties();
  
  public void init() throws ServletException {
    InputStream input = null;
    try {
      input = ServletContext.class.getResourceAsStream("/config.properties");
      prop.load(input);        
    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
  }
    
  
  public ArrayList<Message> getMessages () {
    DbMgmt dbmgmt = new DbMgmt(prop);
    ArrayList<Message> messageList = dbmgmt.getMessages();
        
    return messageList;    
  }  // public ArrayList<Message> getMessages ()
  
  public ArrayList<Project> getProjects () {
    DbMgmt dbmgmt = new DbMgmt(prop);
    ArrayList<Project> projectList = dbmgmt.getProjects();
        
    return projectList;    
  }
  
  
  public Integer createNewMessage (String newNote, Integer projectID) {
    DbMgmt dbmgmt = new DbMgmt(prop);
    int newID = dbmgmt.insertMessage (newNote, projectID);
               
    return newID;    
  } // public createNewMessage (String newNote)
  
  public Boolean removeMessage (Integer messageID) {
    DbMgmt dbmgmt = new DbMgmt(prop);
    dbmgmt.removeMessage (messageID);
            
    return true; 
  } // public Boolean removeMessage (String messageID)
  
  public Boolean updateMessage (Message newMessage) {
    DbMgmt dbmgmt = new DbMgmt(prop);
    dbmgmt.updateMessage (newMessage.getMessageID(), newMessage.getNote());
       
    return true;  
  } // public Boolean updateMessage (Message newMessage)
  
}  // public class MessageServiceImpl
