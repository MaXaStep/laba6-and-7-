package DAO;

import Interfaces.DataAccessObject;
import Models.Order;
import Models.Provider;
import Utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProviderDAO implements DataAccessObject<Provider> {
    public void save(Provider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(provider);
        tx1.commit();
        session.close();
    }

    public void update(Provider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(provider);
        tx1.commit();
        session.close();
    }

    public void delete(Provider provider) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(provider);
        tx1.commit();
        session.close();
    }

    public Provider findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Provider provider = session.get(Provider.class, id);
        session.close();
        return provider;
    }

    public List<Provider> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Provider> customers = (List<Provider>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Provider").list();
        session.close();
        return customers;
    }

    public List<Order> findOrdersById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Order> orders = (List<Order>) session.createQuery("From Order where Provider_id_provider=" + id).list();
        session.close();
        return orders;
    }
}
