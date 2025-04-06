package md.ceiti.ms.hibernate.model.dao;

import md.ceiti.ms.hibernate.model.entity.Sales;
import java.util.List;

public interface SalesDAO extends BaseDAO<Sales,String> {
    public List<Sales> todaySales();
    public List<Sales> topSales();
}
