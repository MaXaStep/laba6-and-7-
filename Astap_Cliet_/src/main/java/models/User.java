package models;

import java.util.HashSet;
import java.util.Set;

public class User extends Object {
    private int fId;
    private String fLogin;
    private String fPassword;
    private String fUsername;
    private String fRole;
    private Set<Operation> fOperations = new HashSet<Operation>(0);
    private Customer fCustomer;

    public User() {

    }

    public User(String fLogin, String fPassword, String fUsername, String fRole) {
        this.fLogin = fLogin;
        this.fPassword = fPassword;
        this.fUsername = fUsername;
        this.fRole = fRole;
    }

    public User(String fLogin, String fPassword, String fUsername, String fRole, int fId) {
        this.fLogin = fLogin;
        this.fPassword = fPassword;
        this.fUsername = fUsername;
        this.fRole = fRole;
        this.fId = fId;
    }
    public String getfRole() {
        return fRole;
    }

    public void setfRole(String fRole) {
        this.fRole = fRole;
    }

    public String getfPassword() {
        return fPassword;
    }

    public void setfPassword(String fPassword) {
        this.fPassword = fPassword;
    }


    public String getfUsername() {
        return fUsername;
    }

    public void setfUsername(String fUsername) {
        this.fUsername = fUsername;
    }

    public String getfLogin() {
        return fLogin;
    }

    public void setfLogin(String fLogin) {
        this.fLogin = fLogin;
    }

    public int getfId() {
        return fId;
    }

    public Set<Operation> getfOperations() {
        return fOperations;
    }

    public void setfOperations(Set<Operation> operations) {
        this.fOperations = operations;
    }

    public Customer getfCustomer() {
        return fCustomer;
    }

    public void setfCustomer(Customer fCustomer) {
        this.fCustomer = fCustomer;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
