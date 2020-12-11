package DAO;

import Interfaces.DataAccessObject;
import Models.Order;
import Utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderDAO implements DataAccessObject<Order> {
    public void save(Order order) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(order);
        tx1.commit();
        session.close();
    }

    public void update(Order order) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(order);
        tx1.commit();
        session.close();
    }

    public void delete(Order order) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(order);
        tx1.commit();
        session.close();
    }

    public Order findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Order order = session.get(Order.class, id);
        session.close();
        return order;
    }

    public List<Order> findAll() {
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Order> orders = (List<Order>)  session.createQuery("From Order").list();
        session.close();
        return orders;
    }
}
