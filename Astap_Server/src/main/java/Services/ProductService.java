package Services;

import DAO.ProductDAO;
import Interfaces.Service;
import Models.Operation;
import Models.Product;

import java.util.List;

public class ProductService implements Service<Product> {
    private ProductDAO productsDAO = new ProductDAO();

    public Product findEntity(int id) {
        return productsDAO.findById(id);
    }

    public void saveEntity(Product entity) {
        productsDAO.save(entity);
    }

    public void deleteEntity(Product entity) {
        productsDAO.delete(entity);
    }

    public void updateEntity(Product entity) {
        productsDAO.update(entity);
    }

    public List<Product> findAllEntities() {
        return productsDAO.findAll();
    }

    public List<Operation> findAllOperations(int id) {
        return productsDAO.findOperationsById(id);
    }
}
