package md.ceiti.ms.hibernate.model.dao;

import md.ceiti.ms.hibernate.model.entity.Toys;

import java.util.List;

public interface ToysDAO extends BaseDAO<Toys,String> {
    public List<Toys> producedMD();

    List<Toys> findAllAll();

    Toys getToyById(int id);
    void insert(Toys toys);
    void update(Toys toys);
    void delete(Toys toys);
    void buy(Toys toys);
    List<Toys> search(String country, String name);

    List<Toys> searchOos(String country, String name);

    List<Toys> findAllOos();
}
