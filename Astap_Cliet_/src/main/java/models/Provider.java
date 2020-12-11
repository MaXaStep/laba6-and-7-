package models;

import java.util.HashSet;
import java.util.Set;

public class Provider {
    private int fId;
    private String fName;
    private String fAddress;
    private String fPhonenumber;
    private String fEmail;
    private Set<Order> fOrders = new HashSet<Order>(0);
    public Provider(){

    }
    public Provider(int fId){
        this.fId = fId;
    }
    public Provider( String fName, String fAddress, String fPhonenumber, String fEmail) {
        this.fName = fName;
        this.fAddress = fAddress;
        this.fPhonenumber = fPhonenumber;
        this.fEmail = fEmail;
    }
    public Provider( String fName, String fAddress, String fPhonenumber, String fEmail,int fId) {
        this.fName = fName;
        this.fAddress = fAddress;
        this.fPhonenumber = fPhonenumber;
        this.fEmail = fEmail;
        this.fId = fId;
    }

    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public String getfPhonenumber() {
        return fPhonenumber;
    }

    public void setfPhonenumber(String fPhonenumber) {
        this.fPhonenumber = fPhonenumber;
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
