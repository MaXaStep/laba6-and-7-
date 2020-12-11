package models;

import java.util.HashSet;
import java.util.Set;

public class Customer {
    private int fId;
    private String fName;
    private String fAddress;
    private String fPhoneNumber;
    private String fEmail;
    private User fUser;
    private Set<Order> fOrders = new HashSet<Order>(0);
    public Customer(){
    }
    public Customer(String fName,String fAddress,String fPhoneNumber, String fEmail, User fUser){
        this.fAddress = fAddress;
        this.fEmail = fEmail;
        this.fPhoneNumber = fPhoneNumber;
        this.fUser = fUser;
        this.fName = fName;
    }
    public Customer(int fId){
        this.fId = fId;
    }
    public Customer(String fName,String fAddress,String fPhoneNumber, String fEmail){
        this.fAddress = fAddress;
        this.fEmail = fEmail;
        this.fPhoneNumber = fPhoneNumber;
        this.fName = fName;
    }
    public Customer(String fName,String fAddress,String fPhoneNumber, String fEmail,int fId){
        this.fAddress = fAddress;
        this.fEmail = fEmail;
        this.fPhoneNumber = fPhoneNumber;
        this.fName = fName;
        this.fId = fId;
    }
    public User getfUser() {
        return fUser;
    }

    public void setfUser(User fUser) {
        this.fUser = fUser;
    }

    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public String getfPhoneNumber() {
        return fPhoneNumber;
    }

    public void setfPhoneNumber(String fPhoneNumber) {
        this.fPhoneNumber = fPhoneNumber;
    }
    public String getfAddress() {
        return fAddress;
    }

    public void setfAddress(String fAddress) {
        this.fAddress = fAddress;
    }
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    public int getfId() {
        return fId;
    }
    public Set<Order> getfOrders() {
        return fOrders;
    }

    public void setfOrders(Set<Order> fOrders) {
        this.fOrders = fOrders;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
