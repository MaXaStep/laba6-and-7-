package DAO;

import Interfaces.DataAccessObject;
import Models.Operation;
import Models.Order;
import Utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OperationDAO implements DataAccessObject<Operation> {
    public void save(Operation operation) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(operation);
        tx1.commit();
        session.close();
    }

    public void update(Operation operation) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(operation);
        tx1.commit();
        session.close();
    }

    public void delete(Operation operation) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(operation);
        tx1.commit();
        session.close();
    }

    public Operation findById(int id) {
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Operation operation = session.get(Operation.class, id);
        session.close();
        return operation;
    }

    public List<Operation> findAll() {
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Operation> operations = (List<Operation>)  session.createQuery("From Operation").list();
        session.close();
        return operations;
    }
}
