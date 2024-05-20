import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ActionWindowController implements Initializable{

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;
    
    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;
    
    private boolean editAction;
    private Person p;
    
    String buttonName;

    @FXML
    void cancelButtonPressed(ActionEvent event) {
    	Stage stage = (Stage)cancelButton.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void okButtonPressed(ActionEvent event) {
    	nameLabel.setText("");
    	phoneLabel.setText("");
    	
    	String name = nameField.getText().trim();
    	String phone = phoneField.getText().trim();
		try {
			@SuppressWarnings("unused")
			int p = Integer.parseInt(phone);
		}catch(NumberFormatException ex) {
			System.out.println("Phone number must include only numbers!");
			phoneLabel.setText("Enter correct phone number");
			phoneLabel.setTextFill(Color.RED);
		}
    	if(name.length()<1) {	
    		nameLabel.setText("Enter correct name");
    		nameLabel.setTextFill(Color.RED);
    	}
    	
    	if(nameLabel.getText().equals("") && phoneLabel.getText().equals("")) {
    		PhonebookController phonebookController = new PhonebookController();
    		//chose which action is chosen(add or edit)
    		if(editAction) 
    			phonebookController.editPhone(p,phone);
    		else
    			phonebookController.addPerson(new Person(name, phone));
    		Stage stage = (Stage)okButton.getScene().getWindow();
        	stage.close();
    	}
    	
    }
    
    void updatePhone(Person p) {
    	this.p = p;
    	nameField.setText(p.getName());
    	nameField.setEditable(false);
    	editAction = true;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//TODO
    }

}
