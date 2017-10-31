package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.todoit.training.client.Message;
import com.todoit.training.client.MessageService;
// import java.io.FileNotFoundException;
//import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileWriter;
import java.io.IOException;


import org.apache.commons.csv.CSVPrinter;

public class MessageServiceImpl extends RemoteServiceServlet implements MessageService{
	public MessageServiceImpl() {
		String pth = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		pth = pth.substring(1); //trim leading slash
		pth += "../notes.csv"; //get out of "classes dir and add proper filename
		notesPath = pth;
	}
	
	private static String notesPath;
	private static final long serialVersionUID = 1L;

	public Message getMessage(ArrayList<String> input) {
		
		if (input != null) {
			
		final String NEW_LINE_SEPARATOR = "\n";
		final Object [] FILE_HEADER = {"Col1"};
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
		
		try {
			fileWriter = new FileWriter(notesPath);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			csvFilePrinter.printRecord(FILE_HEADER);
			
			for (int i = 0; i < input.size(); i++) {
				Object [] line  = {input.get(i)};
				csvFilePrinter.printRecord(line);				
			}
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
				} catch (IOException e) {
				    System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				    e.printStackTrace();
				}
		}
		}
		
		
		
		// String notatka = new String();
		ArrayList<String> notes = new ArrayList<>();
		try {	
		   CSVParser parser = new CSVParser (new FileReader(notesPath), CSVFormat.DEFAULT.withHeader("Col1").withDelimiter(';'));		
		   for (CSVRecord record : parser) {
	            notes.add(record.get("Col1"));	           
	        }				
		   parser.close();		   
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		
		// String messageString = "Hello " + input + "!" + notatka;
		Message message = new Message();
		
		// notes = input;
		
		message.setMessage(notes);
		return message;
	}
}
