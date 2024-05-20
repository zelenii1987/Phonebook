//import javafx.beans.property.SimpleStringProperty;

public class Person {
	private final String name;
	private String phone;
	
	public Person(String name, String phone) {
		this.name = name;
		try {
			@SuppressWarnings("unused")
			int p = Integer.parseInt(phone);
		}catch(NumberFormatException ex) {
			System.out.println("Phone number must include only numbers!");
		}
		this.phone = phone;
	}
	
	public void setPhone(String phone) {
		try {
			@SuppressWarnings("unused")
			int p = Integer.parseInt(phone);
		}catch(NumberFormatException ex) {
			System.out.println("Phone number must include only numbers!");
		}
		this.phone=phone;	
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
}
