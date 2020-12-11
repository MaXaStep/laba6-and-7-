package Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Provider")
public class Provider {
    private int fId;
    private String fName;
    private String fAddress;
    private String fPhonenumber;
    private String fEmail;
    private Set<Order> fOrders = new HashSet<Order>(0);
    public Provider(){

    }
    public Provider( String fName, String fAddress, String fPhonenumber, String fEmail, int fId) {
        this.fName = fName;
        this.fAddress = fAddress;
        this.fPhonenumber = fPhonenumber;
        this.fEmail = fEmail;
        this.fId = fId;
    }

    @Column(name = "provider_email", length = 45)
    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    @Column(name = "provider_phone", length = 45)
    public String getfPhonenumber() {
        return fPhonenumber;
    }

    public void setfPhonenumber(String fPhonenumber) {
        this.fPhonenumber = fPhonenumber;
    }

    @Column(name = "provider_address", length = 45)
    public String getfAddress() {
        return fAddress;
    }

    public void setfAddress(String fAddress) {
        this.fAddress = fAddress;
    }

    @Column(name = "provider_name", length = 45)
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_provider")
    public int getfId() {
        return fId;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fProvider")
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
