package DAO;

import Interfaces.DataAccessObject;
import Models.Customer;
import Models.Order;
import Utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CustomerDAO implements DataAccessObject<Customer> {
    public void save(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().getCurrentSession();
        Transaction tx1 = session.beginTransaction();
        session.save(customer);
        tx1.commit();
        session.close();
    }

    public void update(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(customer);
        tx1.commit();
        session.close();
    }

    public void delete(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(customer);
        tx1.commit();
        session.close();
    }

    public Customer findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Customer customer = session.get(Customer.class, id);
        session.close();
        return customer;
    }

    public List<Customer> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Customer> customers = (List<Customer>) session.createQuery("From Customer").list();
        session.close();
        return customers;
    }

    public List<Order> findOrdersById(int id) {
        return (List<Order>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Order where Customer_id_customer=" + id).list();
    }
}
