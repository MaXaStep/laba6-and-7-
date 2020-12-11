package Services;

import DAO.CustomerDAO;
import DAO.UserDAO;
import Interfaces.Service;
import Models.Customer;
import Models.Operation;
import Models.Order;
import Models.User;

import java.util.List;

public class CustomerService implements Service<Customer> {
    private CustomerDAO customersDao = new CustomerDAO();
    public CustomerService(){
    }
    public Customer findEntity(int id) {
        return customersDao.findById(id);
    }

    public void saveEntity(Customer entity) {
        customersDao.save(entity);
    }

    public void deleteEntity(Customer entity) {
        customersDao.delete(entity);
    }

    public void updateEntity(Customer entity) {
        customersDao.update(entity);
    }

    public List<Customer> findAllEntities() {
        return customersDao.findAll();
    }
    public List<Order> findAllOrders(int id){
        return customersDao.findOrdersById(id);
    }
}
