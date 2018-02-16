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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


public class NotesManager implements EntryPoint {

  private MessageServiceAsync messageService = GWT.create(MessageService.class);
  private ArrayList<Message> messageList = new ArrayList<Message> ();
  private ArrayList<Project> projectList = new ArrayList<Project> ();
  
  private void showPage () {
    
    messageService.getMessages (new AsyncCallback<ArrayList<Message>> () {
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)
      @Override
      public void onSuccess (ArrayList<Message> result) {
        messageList = result;
        
        RootPanel.get("gwtContainer").clear();
        Button newNoteButton = new Button("New note");
        final PopupPanel newNotePopup = new PopupPanel();
        final TextArea newNoteTextArea = new TextArea();
        newNoteTextArea.setCharacterWidth(20);
        newNoteTextArea.setVisibleLines(5);
    
        final VerticalPanel vPanel = new VerticalPanel();
        vPanel.add(newNoteTextArea);
    
        final ListBox projectsListBox = new ListBox ();
        projectsListBox.addItem ("","");
        for (Project project: projectList) {
          projectsListBox.addItem (project.getName (), Integer.toString (project.getID ()));
        }
        projectsListBox.setVisibleItemCount (1);
        vPanel.add (projectsListBox);
    
        Button saveNewNoteButton = new Button("Save note");
        saveNewNoteButton.setWidth("100px");
        vPanel.add(saveNewNoteButton);
        newNotePopup.setWidget(vPanel);
        newNoteButton.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {    
            newNotePopup.center();
          }
        }); // newNoteButton.addClickHandler(new ClickHandler()
    
        saveNewNoteButton.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            int idx = projectsListBox.getSelectedIndex();
            if (idx < 0) {
              Window.alert ("Wybierz projekt!");
              return;
            }
            String value = projectsListBox.getValue (idx);
            if (value.equals ("")) {
              Window.alert ("Wybierz projekt!");
            } else {
              int projectID = Integer.parseInt (value);
              newMessage (newNoteTextArea.getText(), projectID);
              newNotePopup.hide();
            }        
          } // public void onClick(ClickEvent event)
        }); // saveNewNoteButton.addClickHandler(new ClickHandler()
    
    
        FlexTable flexTable = new FlexTable();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(newNoteButton);
        verticalPanel.add(flexTable);
    
        for (int i = 0; i < messageList.size(); i++) {
          final int j = i;
          Message msg = messageList.get(i);
          Button removeButton = new Button ("Remove note " + msg.getMessageID());
          Button updateButton = new Button ("Update note " + msg.getMessageID());
          flexTable.setHTML(i, 0, "" + msg.getMessageID());
          flexTable.setHTML(i, 1, msg.getNote());
          flexTable.setHTML(i, 2, msg.getProjectName());
          flexTable.setWidget(i, 3, removeButton);
          flexTable.setWidget(i, 4, updateButton);
    
          removeButton.addClickHandler (new ClickHandler() {        
            @Override
            public void onClick(ClickEvent event) {
              remMessage (j);
            } // public void onClick(ClickEvent event)
          }); // removeButton[i].addClickHandler (new ClickHandler()
    
     
          updateButton.addClickHandler (new ClickHandler() {
            @Override
            public void onClick (ClickEvent event) {
              final DialogBox updateDialog = new DialogBox();
              final Button saveButton = new Button ("Save note " + messageList.get(j).getMessageID());          
              VerticalPanel vp = new VerticalPanel();
              final TextArea updateNoteTextArea = new TextArea();
              updateNoteTextArea.setText (messageList.get (j).getNote ());
              vp.add(updateNoteTextArea);
              vp.add(saveButton);
          
              updateDialog.add(vp);
                      
              int left = Window.getClientWidth()/ 2;
              int top = Window.getClientHeight()/ 2;
              updateDialog.setPopupPosition(left, top);
              updateDialog.show();
          
              saveButton.addClickHandler (new ClickHandler() {
                @Override
                public void onClick (ClickEvent event) {
                  Message message = new Message (messageList.get(j).getMessageID(), updateNoteTextArea.getText(), 
                    messageList.get(j).getProjectID(), messageList.get(j).getProjectName());              
                  updMessage (message);
                  updateDialog.hide();
                } // public void onClick (ClickEvent event)
              }); // saveButton.addClickHandler (new ClickHandler()
            } // public void onClick (ClickEvent event)
          }); // updateButton[i].addClickHandler (new ClickHandler()
      
    } // for (int i = 0; i < messageList.size(); i++)
    
    RootPanel.get("gwtContainer").add(verticalPanel);
  
    }});
      
  } // private void showPage ()
      
   

  public void onModuleLoad() {
    messageService.getProjects (new AsyncCallback<ArrayList<Project>> () {
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)
      @Override
      public void onSuccess (ArrayList<Project> result) {
        projectList = result;
        showPage ();
      }           
    });    
  } // public void onModuleLoad() 

  private void onFailureAlert (String msg) {
    Window.alert ("Unable to obtain server response: " + msg);
  }
  
  
  private void newMessage (String newNote, int projectID) {
    messageService.createNewMessage (newNote, projectID, new AsyncCallback<Integer> () {  
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)

      @Override
      public void onSuccess (Integer newID) {
        showPage ();               
      } // public void onSuccess (String newID)   
    }); // messageService.createNewMessage (newNoteTextArea.getText(), new AsyncCallback<String> ()
  }
  
  private void remMessage (int index) {
    messageService.removeMessage (messageList.get(index).getMessageID(), new AsyncCallback<Boolean> () {
      @Override
      public void onFailure(Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure(Throwable caught)
      @Override
      public void onSuccess (Boolean result) {
        showPage ();              
      } // public void onSuccess (Boolean result)    
    }); // messageService.removeMessage (messageList.get(j).getMessageID(), new AsyncCallback<Boolean> ()
  }
  
  
  private void updMessage (Message message) {
    messageService.updateMessage (message, new AsyncCallback<Boolean> () {
      @Override
      public void onFailure (Throwable caught) {
        /* server side error occurred */
        onFailureAlert (caught.getMessage());
      } // public void onFailure (Throwable caught)
      @Override
      public void onSuccess (Boolean result) {
        showPage ();      
      } // public void onSuccess (Boolean result)
    }); // messageService.updateMessage (message, new AsyncCallback<Boolean> ()
  }
  
} // public class NotesManager implements EntryPoint

