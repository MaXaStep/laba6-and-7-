package Models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Operation")
public class Operation {
    private int fId;
    private Date fDate;
    private int fProductQuantity;
    private User fUser;
    private Product fProduct;
    private Order fOrder;
    private String fType;
    public Operation(){

    }
    public Operation(Date fDate, int fProductQuantity, String fType, int fId) {
        this.fDate = fDate;
        this.fProductQuantity = fProductQuantity;
        this.fType = fType;
        this.fId = fId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_operation")
    public int getfId() {
        return fId;
    }

    @Column(name = "op_date")
    public Date getfDate() {
        return fDate;
    }

    public void setfDate(Date fDate) {
        this.fDate = fDate;
    }
    @Column(name = "product_quant")
    public int getfProductQuantity() {
        return fProductQuantity;
    }

    public void setfProductQuantity(int fProductQuantity) {
        this.fProductQuantity = fProductQuantity;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Users_id_user")
    public User getfUser() {
        return fUser;
    }
    public void setfUser(User fUser) {
        this.fUser = fUser;
    }
    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "Product_id_product",nullable = false)
    public Product getfProduct() {
        return fProduct;
    }

    public void setfProduct(Product fProduct) {
        this.fProduct = fProduct;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Order_id_order")
    public Order getfOrder() {
        return fOrder;
    }

    public void setfOrder(Order fOrder) {
        this.fOrder = fOrder;
    }
    @Column(name = "type", length = 45)
    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }
}
