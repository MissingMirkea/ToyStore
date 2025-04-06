package md.ceiti.ms.hibernate.model.dao.impl;

import md.ceiti.ms.hibernate.model.dao.BaseDAO;
import md.ceiti.ms.hibernate.model.entity.Sales;
import md.ceiti.ms.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public abstract class BaseDAOImpl<T,ID extends Serializable> implements BaseDAO<T,ID> {

    private final Class<T> entityClass;

    public BaseDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        }
    }

    public abstract List<Sales>getAll();
}
