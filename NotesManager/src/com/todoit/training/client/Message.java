package com.todoit.training.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	// private String message;
	private ArrayList<String> notes;

	public Message(){};

	public void setMessage(ArrayList<String> notes) {
		this.notes = notes;
	}

	public ArrayList<String> getMessage() {
		return notes;
	}
}