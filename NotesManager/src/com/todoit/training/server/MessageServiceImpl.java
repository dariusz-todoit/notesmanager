package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.todoit.training.client.Message;
import com.todoit.training.client.MessageService;
import java.util.UUID;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVPrinter;


public class MessageServiceImpl extends RemoteServiceServlet implements MessageService {

  private ArrayList<Message> messageList = new ArrayList<Message> ();
  private static String notesPath;
  private static final long serialVersionUID = 1L;
  private HashMap<String, Integer> messageIdToRowNumber = new HashMap<>();
  
  
  public MessageServiceImpl() {
    String pth = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();    
    pth = pth.substring(1); //trim leading slash
    pth += "../notes.csv"; //get out of "classes dir and add proper filename
    notesPath = pth;
    System.out.println(pth);
  } // public MessageServiceImpl()


  public ArrayList<Message> getMessages () {    
	CSVParser parser = null;
    try {  
      parser = 
        new CSVParser (new FileReader(notesPath), CSVFormat.DEFAULT.withHeader("ID", "note").withDelimiter(','));    
      for (CSVRecord record : parser) {
        Message m = new Message (record.get("ID"), record.get("note"));
        if (! m.getMessageID().equals("ID")) {
          messageList.add(m);
          messageIdToRowNumber.put (m.getMessageID(), messageList.size() - 1);
        }                     
      }        
    } catch (Exception e){
      System.out.println(e.getMessage());
    } finally {
    	//make sure reader is closed to avoid memory leak
    	if (parser != null) {
    		try {
				parser.close();
			  } catch (IOException e) {
				e.printStackTrace();
			  }
		  }       
    }    
    return messageList;    
  }  // public ArrayList<Message> getMessages ()
  
  
  public String createNewMessage (String newNote) {
    String newID = UUID.randomUUID().toString();
    Message newMessage = new Message (newID, newNote);    
    messageList.add (newMessage);
    messageIdToRowNumber.put (newID, messageList.size() - 1);
    saveCSV ();    
    return newID;    
  } // public String createNewMessage (String newNote)
  
  public Boolean removeMessage (String messageID) {
    int i = messageIdToRowNumber.get (messageID);
    messageList.remove(i);    
    while (i < messageList.size ()) {
      messageIdToRowNumber.put (messageList.get(i).getMessageID(), i);
      i++;
    }
    messageIdToRowNumber.remove (messageID);
    saveCSV ();
    return true; 
  } // public Boolean removeMessage (String messageID)
  
  public Boolean updateMessage (Message newMessage) {
    int i = messageIdToRowNumber.get (newMessage.getMessageID());
    messageList.remove(i);
    messageList.add(i, newMessage);
    saveCSV ();
    return true;  
  } // public Boolean updateMessage (Message newMessage)
  
  
  private void saveCSV () {
    final String NEW_LINE_SEPARATOR = "\n";
    final Object [] FILE_HEADER = {"ID", "note"};
    FileWriter fileWriter = null;
    CSVPrinter csvFilePrinter = null;
    CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator (NEW_LINE_SEPARATOR);
    try {
      fileWriter = new FileWriter(notesPath);
      csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
      csvFilePrinter.printRecord(FILE_HEADER);    
      for (int i = 0; i < messageList.size(); i++) {
        Object [] line  = {messageList.get(i).getMessageID(), messageList.get(i).getNote()};
        csvFilePrinter.printRecord(line);        
      }
      
    } catch (Exception e) {
    System.out.println(e.getMessage());
    } finally {
      try {
        fileWriter.flush();
        fileWriter.close();
        csvFilePrinter.close();
      } catch (IOException e) {
        System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
        e.printStackTrace();
      }
    }
  } // private void saveCSV ()

  
}  // public class MessageServiceImpl
