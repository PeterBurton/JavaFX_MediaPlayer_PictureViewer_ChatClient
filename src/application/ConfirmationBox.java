package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.geometry.*;


public class ConfirmationBox
{

	static Stage window;
	static boolean btnYesClicked;

	public static boolean show(String message, String title,
		String textYes, String textNo)
	{
		btnYesClicked = false;

		
		window = new Stage();
		//set the window to be the only one clickable until the dialog has been confirmed
		window.initModality(Modality.APPLICATION_MODAL);
		//set title and height/width
		window.setTitle(title);
		window.setMinWidth(350);
		window.setMinHeight(200);
		//add paw icon to window
		window.getIcons().add(new Image("file:paw.png"));
		
		Label lbl = new Label();
		lbl.setText(message);

		Button btnYes = new Button();
		btnYes.setText(textYes);
		btnYes.setOnAction(e -> btnYes_Clicked() );

		Button btnNo = new Button();
		btnNo.setText(textNo);
		btnNo.setOnAction(e -> btnNo_Clicked() );

		HBox paneBtn = new HBox(20);
		paneBtn.getChildren().addAll(btnYes, btnNo);
		paneBtn.setAlignment(Pos.CENTER);

		VBox pane = new VBox(20);
		pane.getChildren().addAll(lbl, paneBtn);
		pane.setAlignment(Pos.CENTER);

		Scene scene = new Scene(pane);
		window.setScene(scene);
		//this puts everything else on hold until window is dealt with
		window.showAndWait();
		return btnYesClicked;
	}

	private static void btnYes_Clicked()
	{
		window.close();
		btnYesClicked = true;
	}

	private static void btnNo_Clicked()
	{
		window.close();
		btnYesClicked = false;
	}

}
