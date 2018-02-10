package com.todoit.training.client;


import java.util.ArrayList;
import com.todoit.training.client.Project;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("message")
public interface MessageService extends RemoteService {
  ArrayList<Message> getMessages ();
  ArrayList<Project> getProjects ();
  Integer createNewMessage (String newNote, Integer projectID);
  Boolean removeMessage (Integer messageID);
  Boolean updateMessage (Message newMessage);
} // public interface MessageService extends RemoteService
