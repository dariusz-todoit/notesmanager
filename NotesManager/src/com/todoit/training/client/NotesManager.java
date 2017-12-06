package com.todoit.training.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
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
    }); // b1.addClickHandler(new ClickHandler()
    
    redButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        newMessage (textArea1.getText());
        myPopup.hide();
      } // public void onClick(ClickEvent event)
    }); // redButton.addClickHandler(new ClickHandler()
    
    
    FlexTable flexTable = new FlexTable();
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(b1);
    verticalPanel.add(flexTable);
    
    for (int i = 0; i < messageList.size(); i++) {
      final int j = i;
      Message msg = messageList.get(i);
      Button removeButton = new Button ("Remove note " + msg.getMessageID());
      Button updateButton = new Button ("Update note " + msg.getMessageID());
      flexTable.setHTML(i, 0, "" + msg.getMessageID());
      flexTable.setHTML(i, 1, msg.getNote());
      flexTable.setWidget(i, 2, removeButton);
      flexTable.setWidget(i, 3, updateButton);
    
      removeButton.addClickHandler (new ClickHandler() {        
        @Override
        public void onClick(ClickEvent event) {
          remMessage (j);
        } // public void onClick(ClickEvent event)
      }); // removeButton[i].addClickHandler (new ClickHandler()
    
     
      updateButton.addClickHandler (new ClickHandler() {
        @Override
        public void onClick (ClickEvent event) {
          final DialogBox myDialog = new DialogBox();
          final Button saveButton = new Button ("Save note " + messageList.get(j).getMessageID());          
          VerticalPanel vp = new VerticalPanel();
          final TextArea textArea2 = new TextArea();
          textArea2.setText (messageList.get (j).getNote ());
          vp.add(textArea2);
          vp.add(saveButton);
          
          myDialog.add(vp);
                      
          int left = Window.getClientWidth()/ 2;
          int top = Window.getClientHeight()/ 2;
          myDialog.setPopupPosition(left, top);
          myDialog.show();
          
          saveButton.addClickHandler (new ClickHandler() {
            @Override
            public void onClick (ClickEvent event) {
              final Message message = new Message (messageList.get(j).getMessageID(), textArea2.getText());              
              updMessage (j, message);
              myDialog.hide();
            } // public void onClick (ClickEvent event)
          }); // saveButton.addClickHandler (new ClickHandler()
        } // public void onClick (ClickEvent event)
      }); // updateButton[i].addClickHandler (new ClickHandler()
      
    } // for (int i = 0; i < messageList.size(); i++)
    
    RootPanel.get("gwtContainer").add(verticalPanel);    
  } // private void showPage ()

  public void onModuleLoad() {
    messageService.getMessages (new AsyncCallback<ArrayList<Message>> () {
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)
      @Override
      public void onSuccess (ArrayList<Message> result) {
        messageList = result;
        showPage ();               
      } // public void onSuccess (ArrayList<Message> result)
      
    });// messageService.getMessages (new AsyncCallback<ArrayList<Message>> ()
  } // public void onModuleLoad() 

  private void onFailureAlert (String msg) {
    Window.alert ("Unable to obtain server response: " + msg);
  }
  
  private void newMessage (String newNote) {
    final String newNote1 = newNote;
    messageService.createNewMessage (newNote, new AsyncCallback<String> () {  
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)

      @Override
      public void onSuccess (String newID) {
        Message message = new Message (newID, newNote1);           
        messageList.add (message);                
        showPage ();               
      } // public void onSuccess (String newID)   
    }); // messageService.createNewMessage (textArea1.getText(), new AsyncCallback<String> ()
  }
  
  private void remMessage (int index) {
    final int index1 = index;
    messageService.removeMessage (messageList.get(index).getMessageID(), new AsyncCallback<Boolean> () {
      @Override
      public void onFailure(Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure(Throwable caught)
      @Override
      public void onSuccess (Boolean result) {
        messageList.remove(index1);
        showPage ();              
      } // public void onSuccess (Boolean result)    
    }); // messageService.removeMessage (messageList.get(j).getMessageID(), new AsyncCallback<Boolean> ()
  }
  
  private void updMessage (int index, Message message) {
    final int index1 = index;
    final Message message1 = message;
    messageService.updateMessage (message, new AsyncCallback<Boolean> () {
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)
      @Override
      public void onSuccess (Boolean result) {
        messageList.remove (index1);
        messageList.add (index1, message1);
        showPage ();      
      } // public void onSuccess (Boolean result)
    }); // messageService.updateMessage (message, new AsyncCallback<Boolean> ()
  }
  
} // public class NotesManager implements EntryPoint

