package Interfaces;

import java.util.List;

public interface DataAccessObject<T> {
    void save(T obj);
    void update(T obj);
    void delete(T obj);
    T findById(int id);
    List<T> findAll();
}
