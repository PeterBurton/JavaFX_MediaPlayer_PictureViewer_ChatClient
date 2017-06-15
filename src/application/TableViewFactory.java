package application;

import java.io.File;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewFactory {
	
	
	
	public  TableViewFactory (TableView<File> table, File directory){
		
		//File dir =  new File(directory);
		factory(table, directory);
		table.getSelectionModel().selectFirst();
		
	}
	
	@SuppressWarnings("unchecked")
	public TableView<File> factory(TableView<File> table, File dir)
	{
		//Clear anything previously in the table
		table.getColumns().clear();
		
		/*Setup table columns with the type of object they are getting information from 
		 * and the data type of that information. Also set width and the attribute in the 
		 * object that the table will be getting the data from*/
		
		 TableColumn<File, String> nameColumn = new TableColumn<>("File Name");
	        nameColumn.setMinWidth(200);
	        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

	        table.getItems().addAll(dir.listFiles());
		 
		
		//make sure the table can only contain one set of columns
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//add all columns to the table
		table.getColumns().addAll( nameColumn);
		//return table to calling method
		return table;

	}
}
