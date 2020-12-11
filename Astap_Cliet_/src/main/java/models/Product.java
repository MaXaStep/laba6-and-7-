package models;

import java.util.HashSet;
import java.util.Set;

public class Product {
    private int fId;
    private String fName;
    private String fType;
    private Set<Operation> fOperations = new HashSet<Operation>(0);
    public Product(){

    }
    public Product(String fName, String fType) {
        this.fName = fName;
        this.fType = fType;
    }
    public Product(String fName, String fType,int fId) {
        this.fName = fName;
        this.fType = fType;
        this.fId=fId;
    }
    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
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

    public Set<Operation> getfOperations() {
        return fOperations;
    }

    public void setfOperations(Set<Operation> fOperations) {
        this.fOperations = fOperations;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
