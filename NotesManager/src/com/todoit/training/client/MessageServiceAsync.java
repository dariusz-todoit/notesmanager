package com.todoit.training.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessageServiceAsync {
  void getMessages (AsyncCallback<ArrayList<Message>> callback);  
  void createNewMessage (String newNote, AsyncCallback<Integer> callback);
  void removeMessage (Integer messageID, AsyncCallback<Boolean> callback);
  void updateMessage (Message newMessage, AsyncCallback<Boolean> callback);  
} // public interface MessageServiceAsync
