package Models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Order\"")
public class Order {
    private int fId;
    private String fStatus;
    private Provider fProvider;
    private Customer fCustomer;
    private Set<Operation> fOperation= new HashSet<Operation>(0);

    public Order(String fStatus,int fId) {
        this.fStatus = fStatus;
        this.fId = fId;
    }
    public Order(){

    }
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "fOrder")
    public Set<Operation> getfOperation() {
        return fOperation;
    }
    public void setfOperation(Set<Operation> fOperation) {
        this.fOperation = fOperation;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Customer_id_customer", nullable = false)
    public Customer getfCustomer() {
        return fCustomer;
    }

    public void setfCustomer(Customer fCustomer) {
        this.fCustomer = fCustomer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Provider_id_provider", nullable = false)
    public Provider getfProvider() {
        return fProvider;
    }

    public void setfProvider(Provider fProvider) {
        this.fProvider = fProvider;
    }

    @Column(name = "status", length = 45)
    public String getfStatus() {
        return fStatus;
    }

    public void setfStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_order")
    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
