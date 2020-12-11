package Services;

import DAO.ProductDAO;
import DAO.ProviderDAO;
import Interfaces.Service;
import Models.Order;
import Models.Provider;

import java.util.List;

public class ProviderService implements Service<Provider> {
    private ProviderDAO providersDao = new ProviderDAO();

    public Provider findEntity(int id) {
        return providersDao.findById(id);
    }

    public void saveEntity(Provider entity) {
        providersDao.save(entity);
    }

    public void deleteEntity(Provider entity) {
        providersDao.delete(entity);
    }

    public void updateEntity(Provider entity) {
        providersDao.update(entity);
    }

    public List<Provider> findAllEntities() {
        return providersDao.findAll();
    }
    public List<Order> findAllOrder(int id){
        return providersDao.findOrdersById(id);
    }
}
