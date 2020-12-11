package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserModel {
    private SimpleIntegerProperty Id;
    private SimpleStringProperty Login;
    private SimpleStringProperty Password;
    private SimpleStringProperty Username;
    private SimpleStringProperty Role;
    private SimpleStringProperty Customer;

    public UserModel(int id, String login, String password, String username, String role) {
        Id = new SimpleIntegerProperty(id);
        Login = new SimpleStringProperty(login);
        Password = new SimpleStringProperty(password);
        Username = new SimpleStringProperty(username);
        Role = new SimpleStringProperty(role);
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

    public String getLogin() {
        return Login.get();
    }

    public SimpleStringProperty loginProperty() {
        return Login;
    }

    public String getPassword() {
        return Password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password.set(password);
    }

    public String getUsername() {
        return Username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username.set(username);
    }

    public String getRole() {
        return Role.get();
    }

    public SimpleStringProperty roleProperty() {
        return Role;
    }

    public void setRole(String role) {
        this.Role.set(role);
    }

    public String getCustomer() {
        return Customer.get();
    }

    public SimpleStringProperty customerProperty() {
        return Customer;
    }

    public void setCustomer(String customer) {
        this.Customer.set(customer);
    }
}
