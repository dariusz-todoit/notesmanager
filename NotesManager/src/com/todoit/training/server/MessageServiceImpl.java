package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.todoit.training.client.Message;
import com.todoit.training.client.MessageService;
import java.util.ArrayList;
import com.todoit.training.server.DbMgmt;



public class MessageServiceImpl extends RemoteServiceServlet implements MessageService {

  private static final long serialVersionUID = 1L;
    
  
  public ArrayList<Message> getMessages () {
    DbMgmt dbmgmt = new DbMgmt();
    ArrayList<Message> messageList = dbmgmt.getMessages();
    dbmgmt.closeDbMgmt();
    
    return messageList;    
  }  // public ArrayList<Message> getMessages ()
  
  
  public Integer createNewMessage (String newNote) {
    DbMgmt dbmgmt = new DbMgmt();
    int newID = dbmgmt.insertMessage(newNote);
    dbmgmt.closeDbMgmt();    
           
    return newID;    
  } // public createNewMessage (String newNote)
  
  public Boolean removeMessage (Integer messageID) {
    DbMgmt dbmgmt = new DbMgmt();
    dbmgmt.removeMessage (messageID);
    dbmgmt.closeDbMgmt();
        
    return true; 
  } // public Boolean removeMessage (String messageID)
  
  public Boolean updateMessage (Message newMessage) {
    DbMgmt dbmgmt = new DbMgmt();
    dbmgmt.updateMessage (newMessage.getMessageID(), newMessage.getNote());
    dbmgmt.closeDbMgmt();
    
    return true;  
  } // public Boolean updateMessage (Message newMessage)
  
}  // public class MessageServiceImpl
