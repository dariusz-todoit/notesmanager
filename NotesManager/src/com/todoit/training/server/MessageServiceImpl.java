package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.todoit.training.client.Message;
import com.todoit.training.client.MessageService;
// import java.io.FileNotFoundException;
//import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileWriter;
import java.io.IOException;


import org.apache.commons.csv.CSVPrinter;

public class MessageServiceImpl extends RemoteServiceServlet implements MessageService {
  
  public MessageServiceImpl() {
    String pth = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    
    pth = pth.substring(1); //trim leading slash
    pth += "../notes.csv"; //get out of "classes dir and add proper filename
    notesPath = pth;
    System.out.println(pth);
  } // public MessageServiceImpl()
  
  private static String notesPath;
  private static final long serialVersionUID = 1L;

  public ArrayList<Message> getMessages () {
	  
    Message firstNote = new Message ();
    firstNote.setMessage ("1", "Pewien tekst");
    ArrayList<Message> messages = new ArrayList<Message>();
    messages.add(firstNote);
    
    return messages;    
  }  // public ArrayList<Message> getMessages ()
  
  public void createMessage (Message newMessage) { 
	  
  
  }
  
  public ArrayList<Message> createTest (Message newMessage) {
	  Message firstNote = new Message ();
     firstNote.setMessage ("1", "Pewien tekst");
	    ArrayList<Message> messages = new ArrayList<Message>();
	    messages.add(firstNote);
	    messages.add(newMessage);
	    
	    return messages;  
  } // public ArrayList<Message> createTest (Message newMessage)
  
}  // public class MessageServiceImpl
