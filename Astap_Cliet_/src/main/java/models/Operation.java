package models;

import java.sql.Date;

public class Operation {
    private int fId;
    private Date fDate;
    private int fProductQuantity;
    private User fUser;
    private Product fProduct;
    private Order fOrder;
    private String fType;
    public Operation(){

    }
    public Operation(Date fDate, int fProductQuantity, String fType) {
        this.fDate = fDate;
        this.fProductQuantity = fProductQuantity;
        this.fType = fType;
    }
    public int getfId() {
        return fId;
    }

    public Date getfDate() {
        return fDate;
    }

    public void setfDate(Date fDate) {
        this.fDate = fDate;
    }
    public int getfProductQuantity() {
        return fProductQuantity;
    }

    public void setfProductQuantity(int fProductQuantity) {
        this.fProductQuantity = fProductQuantity;
    }
    public User getfUser() {
        return fUser;
    }
    public void setfUser(User fUser) {
        this.fUser = fUser;
    }
    public Product getfProduct() {
        return fProduct;
    }

    public void setfProduct(Product fProduct) {
        this.fProduct = fProduct;
    }
    public Order getfOrder() {
        return fOrder;
    }

    public void setfOrder(Order fOrder) {
        this.fOrder = fOrder;
    }
    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
