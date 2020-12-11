package DAO;

import Interfaces.DataAccessObject;
import Models.Operation;
import Models.Order;
import Models.Product;
import Utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDAO implements DataAccessObject<Product> {
    public void save(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(product);
        tx1.commit();
        session.close();
    }

    public void update(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(product);
        tx1.commit();
        session.close();
    }

    public void delete(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(product);
        tx1.commit();
        session.close();
    }

    public Product findById(int id) {
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Product product = session.get(Product.class, id);
        session.close();
        return product;  }

    public List<Product> findAll() {
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Product> products = (List<Product>)session.createQuery("From Product").list();
        session.close();
        return products;
    }
    public List<Operation> findOperationsById(int id){
        Session session =   HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Operation> operations = (List<Operation>) session.createQuery("From Operation where Product_id_product="+id).list();
        session.close();
        return operations;
    }
}
