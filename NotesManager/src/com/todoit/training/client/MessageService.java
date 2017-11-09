package com.todoit.training.client;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("message")
public interface MessageService extends RemoteService {
	ArrayList<Message> getMessages ();
	void createMessage (Message newMessage);
	ArrayList<Message> createTest (Message newMessage);
}
