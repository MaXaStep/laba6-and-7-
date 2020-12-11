package Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
public class User extends Object {
    private int fId;
    private String fLogin;
    private String fPassword;
    private String fUsername;
    private String fRole;
    private Set<Operation> fOperations = new HashSet<Operation>(0);
    private Customer fCustomer;
    public User(){

    }
    public User(String fLogin, String fPassword, String fUsername, String fRole,int fId) {
        this.fLogin = fLogin;
        this.fPassword = fPassword;
        this.fUsername = fUsername;
        this.fRole = fRole;
        this.fId = fId;
    }
    public User(String fLogin, String fPassword, String fUsername, String fRole) {
        this.fLogin = fLogin;
        this.fPassword = fPassword;
        this.fUsername = fUsername;
        this.fRole = fRole;
    }
    @Column(name = "role", length = 45)
    public String getfRole() {
        return fRole;
    }

    public void setfRole(String fRole) {
        this.fRole = fRole;
    }

    @Column(name = "password", length = 45)
    public String getfPassword() {
        return fPassword;
    }

    public void setfPassword(String fPassword) {
        this.fPassword = fPassword;
    }

    @Column(name = "user_name", length = 45)

    public String getfUsername() {
        return fUsername;
    }

    public void setfUsername(String fUsername) {
        this.fUsername = fUsername;
    }

    @Column(name = "login", length = 45)
    public String getfLogin() {
        return fLogin;
    }

    public void setfLogin(String fLogin) {
        this.fLogin = fLogin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    public int getfId() {
        return fId;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fUser")
    public Set<Operation> getfOperations() {
        return fOperations;
    }

    public void setfOperations(Set<Operation> operations) {
        this.fOperations = operations;
    }

    @OneToOne(mappedBy = "fUser")
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
