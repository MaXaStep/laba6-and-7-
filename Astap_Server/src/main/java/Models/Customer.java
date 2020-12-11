package Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Customer")
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
    public Customer(String fName,String fAddress,String fPhoneNumber, String fEmail, int fId){
        this.fAddress = fAddress;
        this.fEmail = fEmail;
        this.fPhoneNumber = fPhoneNumber;
        this.fName = fName;
        this.fId = fId;
    }
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="Users_id_user")
    public User getfUser() {
        return fUser;
    }

    public void setfUser(User fUser) {
        this.fUser = fUser;
    }

    @Column(name = "customer_email",length = 45)
    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    @Column(name = "customer_phone",length = 45)
    public String getfPhoneNumber() {
        return fPhoneNumber;
    }

    public void setfPhoneNumber(String fPhoneNumber) {
        this.fPhoneNumber = fPhoneNumber;
    }
    @Column(name = "customer_address",length = 45)
    public String getfAddress() {
        return fAddress;
    }

    public void setfAddress(String fAddress) {
        this.fAddress = fAddress;
    }
    @Column(name = "customer_name",length = 45)
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_customer")
    public int getfId() {
        return fId;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fCustomer")
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
