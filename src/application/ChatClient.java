package application;

import java.io.*;
import java.net.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ChatClient {

	TextArea incoming;
	TextField outgoing;
	BufferedReader reader;
	PrintWriter	writer;
	Socket sock;

	

	
	public  ChatClient(BorderPane pane) {

		Label incomingMessages = new Label("Incoming Messages:");		
		incoming = new TextArea();
		incoming.setPrefHeight(500);
		incoming.setEditable(false);
		incoming.setWrapText(true);
		VBox topBox = new VBox(incomingMessages,incoming);
		Label outgoingMessages = new Label("Outgoing Messages:");
		outgoing = new TextField();
		VBox midBox = new VBox(outgoingMessages, outgoing);
		Button sendButton = new Button("send");
		sendButton.setOnAction(e->{
			try {
				writer.println(outgoing.getText());
				writer.flush();
			}catch (Exception z){
				z.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
		});
		HBox box = new HBox(sendButton);
		box.setAlignment(Pos.CENTER);
		setUpNetworking();
		VBox allBox = new VBox(10,topBox,midBox,box);
		//allBox.setPadding(new Insets(10, 10, 10, 10));
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.setCenter(allBox);
		

	}
	
	private void setUpNetworking(){
		try{
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking Established");
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
	
	public class IncomingReader implements Runnable{

		public void run() {
			String message;
			try{
				while((message = reader.readLine()) != null){
					System.out.println("read " + message);
					incoming.appendText(message + "\n");
					
				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
			
		}
		
	}
}
