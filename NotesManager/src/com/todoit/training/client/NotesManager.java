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

	private class MessageCallBack implements AsyncCallback<Message> {

		@Override
		public void onFailure(Throwable caught) {
			/* server side error occurred */
			Window.alert("Unable to obtain server response: " + caught.getMessage());
		}

		@Override
		public void onSuccess(Message result) {
			/* server returned result, show user the message */
			/* Window.alert(result.getMessage()); */
			// Label lblName = new Label(result.getMessage().get(0));
			
			final ArrayList<String> notes = result.getMessage();
			if (notes.size() > 0) {
				notes.remove(0);
			}
			
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
					notes.add(textArea1.getText());
					messageService.getMessage(notes, new MessageCallBack());
					myPopup.hide();
				}
			});
			
			
			FlexTable flexTable = new FlexTable();
			VerticalPanel verticalPanel = new VerticalPanel();
			verticalPanel.add(b1);
			verticalPanel.add(flexTable);
			
			Button[] removeButton = new Button[notes.size()];
			Button[] updateButton = new Button[notes.size()];
			for (int i = 0; i < notes.size(); i++) {
				removeButton[i] = new Button ("Remove note " + (i+1));
				updateButton[i] = new Button ("Update note " + (i+1));
				flexTable.setHTML(i, 0, "" + (i + 1));
				flexTable.setHTML(i, 1, notes.get(i));
				flexTable.setWidget(i, 2, removeButton[i]);
				flexTable.setWidget(i, 3, updateButton[i]);
			} // for (int i = 0; i < notes.size(); i++)
			
			
			for (int i = 0; i < notes.size(); i++) {
				final int j = i;
				removeButton[i].addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
					   notes.remove(j);
					   messageService.getMessage(notes, new MessageCallBack());
					}
					});
			
				updateButton[i].addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						final DialogBox myDialog = new DialogBox();
						final Button saveButton = new Button ("Save note " + (j+1));
						
						VerticalPanel vp = new VerticalPanel();
						final TextArea textArea2 = new TextArea();
						textArea2.setText(notes.get(j));
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
							   notes.remove(j);
							   notes.add(j, textArea2.getText());
							   myDialog.hide();
							   messageService.getMessage(notes, new MessageCallBack());	
							}});
					}
					});
			
			
			}
			
			
			
			RootPanel.get("gwtContainer").add(verticalPanel);
			
			
			
			
		   
		}
	} // private class MessageCallBack

	public void onModuleLoad() {
		messageService.getMessage(null, new MessageCallBack());
		
		}  
	}

