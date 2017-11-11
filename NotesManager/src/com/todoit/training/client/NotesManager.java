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
  private ArrayList<Message> messageList = new ArrayList<Message> ();
  
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
        	
          
          // messageService.createTest (message, new AsyncCallback<ArrayList<Message>> () {
          // messageService.createMessage (message, new AsyncCallback<Void> () {
        messageService.createNewMessage (textArea1.getText(), new AsyncCallback<String> () {	
          	@Override
            public void onFailure (Throwable caught) {
              /* server side error occurred */
              Window.alert("Unable to obtain server response: " + caught.getMessage());
            } 
        	
        	@Override
        //    public void onSuccess(ArrayList<Message> result) {
        //     public void onSuccess (Void result) {
        	public void onSuccess (String newID) {
        		Message message = new Message();
            	message.setMessage (newID, textArea1.getText());
        		messageList.add (message);                
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
    
    for (int i = 0; i < messageList.size(); i++) {
      final int j = i;
      removeButton[i].addClickHandler(new ClickHandler() {
        
        @Override
        public void onClick(ClickEvent event) {
         // System.out.println(messageList.get (j).getMessageID());
          messageService.removeMessage (messageList.get(j).getMessageID(), new AsyncCallback<Boolean> () {
            @Override
            public void onFailure(Throwable caught) {
              /* server side error occurred */
              Window.alert("Unable to obtain server response: " + caught.getMessage());
            }
            @Override
            public void onSuccess (Boolean result) {
              messageList.remove(j);
              showPage ();              
            }
            
         });
        }
       });
    
     
      updateButton[i].addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          final DialogBox myDialog = new DialogBox();
          final Button saveButton = new Button ("Save note " + messageList.get(j).getMessageID());
          
          VerticalPanel vp = new VerticalPanel();
          final TextArea textArea2 = new TextArea();
          textArea2.setText (messageList.get (j).getMessage ());
          vp.add(textArea2);
          vp.add(saveButton);
          
          myDialog.add(vp);
                      
          int left = Window.getClientWidth()/ 2;
          int top = Window.getClientHeight()/ 2;
          myDialog.setPopupPosition(left, top);
          myDialog.show();
          
          saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              final Message message = new Message ();
              message.setMessage (messageList.get(j).getMessageID(), textArea2.getText());
               // notes.remove(j);
               // notes.add(j, textArea2.getText());
               myDialog.hide();
               messageService.updateMessage(message, new AsyncCallback<Boolean> () {
                 @Override
                 public void onFailure (Throwable caught) {
                   /* server side error occurred */
                   Window.alert("Unable to obtain server response: " + caught.getMessage());
                 }
                 @Override
                 public void onSuccess (Boolean result) {
                   messageList.remove(j);
                   messageList.add(j, message);
                   showPage ();              
                 }
                 
                 
                 
               }); 
               
               
               
            }});
        }
        });
    
    
    
    
    
    
    
    
    
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
        public void onSuccess (ArrayList<Message> result) {
    		messageList = result;
    		showPage ();    		   		
    	}
    	
    });    
  } // public void onModuleLoad() 
}

