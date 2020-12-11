package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderModel {
    private SimpleIntegerProperty Id;
    private SimpleStringProperty Status;
    private SimpleStringProperty Provider;
    private SimpleStringProperty Customer;

    public OrderModel(int id, String status, String provider, String customer) {
        Id = new SimpleIntegerProperty(id);
        Status = new SimpleStringProperty(status);
        Provider = new SimpleStringProperty(provider);
        Customer = new SimpleStringProperty(customer);
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

    public String getProvider() {
        return Provider.get();
    }

    public SimpleStringProperty providerProperty() {
        return Provider;
    }

    public void setProvider(String provider) {
        this.Provider.set(provider);
    }

    public String getStatus() {
        return Status.get();
    }

    public SimpleStringProperty statusProperty() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status.set(status);
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
}
