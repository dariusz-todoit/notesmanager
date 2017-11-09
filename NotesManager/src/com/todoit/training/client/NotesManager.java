package com.todoit.training.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotesManager implements EntryPoint {

  private MessageServiceAsync messageService = GWT.create(MessageService.class);
  private ArrayList<Message> messageList;

  private void showPage () {
    RootPanel.get("gwtContainer").clear();
    Button b1 = new Button("New note");
    final PopupPanel myPopup = new PopupPanel();
    final TextArea textArea1 = new TextArea();
    textArea1.setCharacterWidth(20);
    textArea1.setVisibleLines(5);
    
    final VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(textArea1);
    Button redButton = new Button("Save note");
    redButton.setWidth("100px");
    vPanel.add(redButton);
    myPopup.setWidget(vPanel);
    b1.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {    
        myPopup.center();
      }
    });
    
    redButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          Message message = new Message();
          message.setMessage("2", textArea1.getText());
          
          // messageService.createTest (message, new AsyncCallback<ArrayList<Message>> () {
           messageService.createMessage (message, new AsyncCallback<Void> () {
          	@Override
            public void onFailure(Throwable caught) {
              /* server side error occurred */
              Window.alert("Unable to obtain server response: " + caught.getMessage());
            } 
        	
        	@Override
        //    public void onSuccess(ArrayList<Message> result) {
            public void onSuccess() {
        		// messageList = result;
        		showPage ();    		   		
        	}
        	
        });
          myPopup.hide();
        }
      });
    
    
    FlexTable flexTable = new FlexTable();
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(b1);
    verticalPanel.add(flexTable);
    
    Button[] removeButton = new Button[messageList.size()];
    Button[] updateButton = new Button[messageList.size()];
    for (int i = 0; i < messageList.size(); i++) {
      removeButton[i] = new Button ("Remove note " + messageList.get(i).getMessageID());
      updateButton[i] = new Button ("Update note " + messageList.get(i).getMessageID());
      flexTable.setHTML(i, 0, "" + messageList.get(i).getMessageID());
      flexTable.setHTML(i, 1, messageList.get(i).getMessage());
      flexTable.setWidget(i, 2, removeButton[i]);
      flexTable.setWidget(i, 3, updateButton[i]);
    } // for (int i = 0; i < messageList.size(); i++)
    
    
    
    RootPanel.get("gwtContainer").add(verticalPanel);    
  } // private void showPage ()

  public void onModuleLoad() {
    messageService.getMessages (new AsyncCallback<ArrayList<Message>> () {
    	@Override
        public void onFailure(Throwable caught) {
          /* server side error occurred */
          Window.alert("Unable to obtain server response: " + caught.getMessage());
        } 
    	
    	@Override
        public void onSuccess(ArrayList<Message> result) {
    		messageList = result;
    		showPage ();    		   		
    	}
    	
    });    
  } // public void onModuleLoad() 
}

