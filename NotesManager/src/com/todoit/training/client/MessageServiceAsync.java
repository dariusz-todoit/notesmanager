package com.todoit.training.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessageServiceAsync {
	void getMessage(ArrayList<String> input, AsyncCallback<Message> callback);
}
