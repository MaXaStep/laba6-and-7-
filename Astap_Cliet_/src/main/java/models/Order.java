package models;


import java.util.HashSet;
import java.util.Set;

public class Order {
    private int fId;
    private String fStatus;
    private Provider fProvider;
    private Customer fCustomer;
    private Set<Operation> fOperation = new HashSet<Operation>(0);

    public Order(int ProviderId, int CustomerId, String fStatus) {
        this.fStatus = fStatus;
        this.fCustomer = new Customer(CustomerId);
        this.fProvider = new Provider(ProviderId);
    }

    public Order(String fStatus) {
        this.fStatus = fStatus;
    }

    public Order(String fStatus, int fId) {
        this.fStatus = fStatus;
        this.fId = fId;
    }

    public Order() {

    }

    public Set<Operation> getfOperation() {
        return fOperation;
    }

    public void setfOperation(Set<Operation> fOperation) {
        this.fOperation = fOperation;
    }

    public Customer getfCustomer() {
        return fCustomer;
    }

    public void setfCustomer(Customer fCustomer) {
        this.fCustomer = fCustomer;
    }

    public Provider getfProvider() {
        return fProvider;
    }

    public void setfProvider(Provider fProvider) {
        this.fProvider = fProvider;
    }

    public String getfStatus() {
        return fStatus;
    }

    public void setfStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
