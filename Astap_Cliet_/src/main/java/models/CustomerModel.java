package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerModel {
    private SimpleIntegerProperty Id;
    private SimpleStringProperty Name;
    private SimpleStringProperty Address;
    private SimpleStringProperty PhoneNumber;
    private SimpleStringProperty Email;
    private SimpleStringProperty User;


    public CustomerModel(int id, String name, String address,String phoneNumber, String email) {
        Id =new SimpleIntegerProperty(id);
        Name = new SimpleStringProperty(name);
        Address = new SimpleStringProperty(address);
        PhoneNumber = new SimpleStringProperty(phoneNumber);
        Email = new SimpleStringProperty(email);
    }
    public String getPhoneNumber() {
        return PhoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber.set(phoneNumber);
    }

    public String getAddress() {
        return Address.get();
    }

    public SimpleStringProperty addressProperty() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address.set(address);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public int getId() {
        return Id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return Id;
    }

    public void setId(int id) {
        this.Id.set(id);
    }

    public String getEmail() {
        return Email.get();
    }

    public SimpleStringProperty emailProperty() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email.set(email);
    }

    public String getUser() {
        return User.get();
    }

    public SimpleStringProperty userProperty() {
        return User;
    }

    public void setUser(String user) {
        this.User.set(user);
    }
}
