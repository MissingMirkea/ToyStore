package md.ceiti.ms.hibernate.model.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDAO<T,ID extends Serializable> {
    T findById(ID id);
    List<T> findAll();

}
