package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OperationModel {
    private SimpleIntegerProperty Id;
    private SimpleStringProperty Date;
    private SimpleStringProperty Order;
    private SimpleStringProperty Product;
    private SimpleStringProperty Quantity;
    private SimpleStringProperty User;
    private SimpleStringProperty Type;

    public OperationModel(int id, String date, String order, String product, String quantity, String user, String type) {
        Id = new SimpleIntegerProperty(id);
        Date = new SimpleStringProperty(date);
        Order = new SimpleStringProperty(order);
        Product = new SimpleStringProperty(product);
        Quantity = new SimpleStringProperty(quantity);
        User = new SimpleStringProperty(user);
        Type = new SimpleStringProperty(type);
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

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getOrder() {
        return Order.get();
    }

    public SimpleStringProperty orderProperty() {
        return Order;
    }

    public void setOrder(String order) {
        this.Order.set(order);
    }

    public String getProduct() {
        return Product.get();
    }

    public SimpleStringProperty productProperty() {
        return Product;
    }

    public void setProduct(String product) {
        this.Product.set(product);
    }

    public String getQuantity() {
        return Quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        this.Quantity.set(quantity);
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

    public String getType() {
        return Type.get();
    }

    public SimpleStringProperty typeProperty() {
        return Type;
    }

    public void setType(String type) {
        this.Type.set(type);
    }
}
