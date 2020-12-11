package Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Product")
public class Product {
    private int fId;
    private String fName;
    private String fType;
    private Set<Operation> fOperations = new HashSet<Operation>(0);
    public Product(){

    }
    public Product(String fName, String fType,int fId) {
        this.fName = fName;
        this.fType = fType;
        this.fId = fId;
    }
    @Column(name = "product_type",length = 45)
    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }
    @Column(name = "product_name",length = 45)
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product")
    public int getfId() {
        return fId;
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "fProduct")
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
