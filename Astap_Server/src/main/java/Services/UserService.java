package Services;

import DAO.UserDAO;
import Interfaces.Service;
import Models.Operation;
import Models.User;

import java.util.List;

public class UserService implements Service<User> {
    private UserDAO usersDao = new UserDAO();
    public UserService(){
    }
    public User findEntity(int id) {
    return usersDao.findById(id);
    }

    public void saveEntity(User entity) {
        usersDao.save(entity);
    }

    public void deleteEntity(User entity) {
        usersDao.delete(entity);
    }

    public void updateEntity(User entity) {
        usersDao.update(entity);
    }

    public List<User> findAllEntities() {
        return usersDao.findAll();
    }
    public List<Operation> findAllOperations(int id){
        return usersDao.findOperationsById(id);
    }
}
