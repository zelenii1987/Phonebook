import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PhonebookController implements Initializable{

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<Person> phonebookTable;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private TableColumn<Person, String> phoneColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;
    
    private static ObservableList<Person> persons = FXCollections.observableArrayList();
    
    @FXML
    void addButtonPressed(ActionEvent event) throws IOException {
    	try {
    		//get current window stage
    		Node node = (Node)event.getSource();
    		Stage currentStage = (Stage)node.getScene().getWindow();
	    	//load Action window
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ActionWindow.fxml"));
	    	Parent root = loader.load();

	    	//show Action new window	    	
	    	Stage stage = new Stage();
	    	stage.setScene(new Scene(root));
	    	stage.setTitle("Add new person");
	    	//define new window as MODAL (can't interact with main stage)
	    	stage.initModality(Modality.WINDOW_MODAL);
	    	stage.initOwner(currentStage);
	    	stage.show();
    	}catch(IOException ex) {
    		System.err.println(ex);
    	}
    }

    //edit phone number
    @FXML
    void editButtonPressed(ActionEvent event) {
    	try {
    		//get current window stage
    		Node node = (Node)event.getSource();
    		Stage currentStage = (Stage)node.getScene().getWindow();
	    	//load Action window
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ActionWindow.fxml"));
	    	Parent root = loader.load();
	    	
	    	//get controller of Action window scene
	    	ActionWindowController actionController = loader.getController();
	    	//pass name of Person to be changed
	    	phonebookTable.getSelectionModel().getSelectedItem();
	    	actionController.updatePhone(phonebookTable.getSelectionModel().getSelectedItem());
	    	
	    	//show Action new window
	    	
	    	Stage stage = new Stage();
	    	stage.setScene(new Scene(root));
	    	stage.setTitle("Edit phone");
	    	//define new window as MODAL (can't interact with main stage)
	    	stage.initModality(Modality.WINDOW_MODAL);
	    	stage.initOwner(currentStage);
	    	stage.show();
    	}catch(IOException ex) {
    		System.err.println(ex);
    	}
    }
    
    //remove selected Person from table
    @FXML
    void removeButtonPressed(ActionEvent event) {
    	phonebookTable.getItems().remove(phonebookTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void loadButtonPressed(ActionEvent event) {
    	persons.clear();
    	updateTable(getPerson());
    }
    

    @FXML
    void saveButtonPressed(ActionEvent event) throws FileNotFoundException {
    	try {
    		Formatter output = new Formatter("Phonebook.txt");
    		for(Person p: persons)
    			output.format("%s %s\n", p.getName(), p.getPhone());
    		output.close();
    	}catch (FileNotFoundException ex) {
    		System.out.println("File not found");
    	}
    }

    @FXML
    void searchButtonPressed(ActionEvent event) {
    	//phonebookTable.getItems().clear();
    	updateTable(searchList(searchField.getText()));
    	//phonebookTable.setItems(searchList(searchField.getText()));
    }

    @FXML
    void searchFieldPressed(ActionEvent event) {
    	//TODO
    }
    
    public void initialize(URL url, ResourceBundle rb) {
    	//bind action buttons status to selection mode of table(enable if row is selected)
    	editButton.disableProperty().bind(Bindings.isEmpty(phonebookTable.getSelectionModel().getSelectedItems()));
    	removeButton.disableProperty().bind(Bindings.isEmpty(phonebookTable.getSelectionModel().getSelectedItems()));
    	
    	updateTable(getPerson());
    }
    
    //this method return ObservableList of Person objects sorted by "name"
    private ObservableList<Person> getPerson(){    	
    	try {
    		Scanner input = new Scanner(new File("Phonebook.txt"));
    		//read text file, create array contained name(first cells) and phone(in the last cell)
    		while(input.hasNextLine()) {
	        	String str = input.nextLine();
	        	String[] strArray = str.split(" ");
	        	String name = new String();
	        	//create String representation of name
	        	for(int i=0; i<strArray.length-1;i++) {
	        		name+=strArray[i]+" ";
	        	}
	        	
	        	//add Person object to observable list
	        	persons.add(new Person(name.trim(), strArray[strArray.length-1]));	        	
    		}
    		//sort observable list by name
    		persons.sort(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER));
    	}catch(IOException ex) {
        	System.out.println("File not found");
        	//print warning message
        	Label ph = new Label();
        	ph.setText("File not found");
        	ph.setTextFill(Color.RED);
        	phonebookTable.setPlaceholder(ph);
    	}//end try/catch
    	return persons;
    }
    
    //return list of Persons that contains required name (or part of name)
    private ObservableList<Person> searchList(String word){
    	ObservableList<Person> resultList = FXCollections.observableArrayList();
    	
    	for(Person p: persons) {
    		if(p.getName().toLowerCase().contains(word.toLowerCase()))
    			resultList.add(p);
    	}
    	
    	return resultList;
    }
    
	//add new Person in alphabet order (sorted by name)
    void addPerson(Person p) {
    	int index=0;
    	for(Person i: persons) {
    		if(i.getName().toLowerCase().compareTo(p.getName().toLowerCase())>=0) {
    			index=persons.indexOf(i);
    			break;
    		}
    	}
    	persons.add(index, p);
    }
    
    //add updated Person and remove old Person
    void editPhone(Person p, String phone) {
    	int index = persons.indexOf(p);
    	persons.add(index, new Person(p.getName(),phone));
    	persons.remove(index+1);
    }
    
    private void updateTable(ObservableList<Person> list) {
    	//set up the columns in the table
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
    	phoneColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
    	
    	//load data
    	phonebookTable.setItems(list);
    }
}
