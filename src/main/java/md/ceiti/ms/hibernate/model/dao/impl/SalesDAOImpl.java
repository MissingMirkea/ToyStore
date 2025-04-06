package md.ceiti.ms.hibernate.model.dao.impl;

import md.ceiti.ms.hibernate.model.dao.SalesDAO;
import md.ceiti.ms.hibernate.model.entity.Sales;
import md.ceiti.ms.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SalesDAOImpl extends BaseDAOImpl<Sales,String> implements SalesDAO {

    public SalesDAOImpl() {super(Sales.class);}

    @Override
    public List<Sales>getAll(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "Select s FROM Sales s ";
            Query<Sales> query = session.createQuery(hql,Sales.class);
            return query.list();
        }
    }

    @Override
    public List<Sales> todaySales() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Sales s WHERE DATE(transaction_date) = CURRENT_DATE";
            Query<Sales> query = session.createQuery(hql,Sales.class);
            return query.list();
        }
    }

    @Override
    public List<Sales> topSales() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "    SELECT s FROM Sales s ORDER BY total DESC";
            Query<Sales> query = session.createQuery(hql,Sales.class);
            return query.list();
        }
    }
}
