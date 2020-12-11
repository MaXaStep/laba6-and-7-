package Services;

import DAO.OperationDAO;
import Interfaces.Service;
import Models.Operation;

import java.util.List;

public class OperationService implements Service<Operation> {
    private OperationDAO operationsDAO = new OperationDAO();

    public Operation findEntity(int id) {
        return operationsDAO.findById(id);
    }

    public void saveEntity(Operation entity) {
        operationsDAO.save(entity);
    }

    public void deleteEntity(Operation entity) {
        operationsDAO.delete(entity);
    }

    public void updateEntity(Operation entity) {
        operationsDAO.update(entity);
    }

    public List<Operation> findAllEntities() {
        return operationsDAO.findAll();
    }
}
