package Services;

import DAO.OrderDAO;
import Interfaces.Service;
import Models.Order;

import java.util.List;

public class OrderService implements Service<Order> {
    private OrderDAO ordersDAO = new OrderDAO();

    public Order findEntity(int id) {
        return ordersDAO.findById(id);
    }

    public void saveEntity(Order entity) {
        ordersDAO.save(entity);
    }

    public void deleteEntity(Order entity) {
        ordersDAO.delete(entity);
    }

    public void updateEntity(Order entity) {
        ordersDAO.update(entity);
    }

    public List<Order> findAllEntities() {
        return ordersDAO.findAll();
    }
}
