package md.ceiti.ms.hibernate.model.dao.impl;

import md.ceiti.ms.hibernate.model.dao.ToysDAO;
import md.ceiti.ms.hibernate.model.entity.Sales;
import md.ceiti.ms.hibernate.model.entity.Toys;
import md.ceiti.ms.hibernate.model.entity.ToysOutOfStock;
import md.ceiti.ms.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ToysDAOImpl extends BaseDAOImpl<Toys, String> implements ToysDAO {

    public ToysDAOImpl() {super(Toys.class);}

    @Override
    public List<Toys> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        String hql = "Select t FROM Toys t where t.cantity > 0 order by t.id ";
        Query<Toys> query = session.createQuery(hql, Toys.class);
        return query.list();
        }
    }

    @Override
    public List<Toys> producedMD() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Toys t WHERE t.country = :country";
            Query<Toys> query = session.createQuery(hql, Toys.class);
            query.setParameter("country", "MD"); // "MD" reprezintă Moldova
            return query.list();
        }
    }

    public void insertFromOtherTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            String sql = """
            INSERT INTO ToysOutOfStock ( name, country, toyType,age, cantity, price)
            SELECT  name, country, toyType,age, cantity, price 
            FROM Toys
            WHERE NOT EXISTS (
                SELECT 1 FROM Toys WHERE Toys.id = ToysOutOfStock.id
            )
        """;

            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Toys toys) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Salvăm jucăria în tabelul Toys
            session.save(toys);

            // Creăm o copie a obiectului pentru tabelul ToysOutOfStock
            ToysOutOfStock outOfStockToy = new ToysOutOfStock();
            outOfStockToy.setId(toys.getId());
            outOfStockToy.setName(toys.getName());
            outOfStockToy.setCountry(toys.getCountry());
            outOfStockToy.setToyType(toys.getToyType());
            outOfStockToy.setCantity(toys.getCantity());

            // Salvăm și în ToysOutOfStock
            session.save(outOfStockToy);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Toys toys) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(toys); // Actualizează obiectul Toys
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Toys toys) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(toys); // Șterge obiectul Toys
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void buy(Toys toys) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // 1. Preluăm jucăria din baza de date folosind ID-ul
            Toys existingToy = session.get(Toys.class, toys.getId());

            if (existingToy != null && existingToy.getCantity() >= toys.getCantity()) {
                // 2. Reducem cantitatea jucăriei
                existingToy.setCantity(existingToy.getCantity() - toys.getCantity());

                // 3. Calculăm totalul vânzării
                float total = toys.getCantity() * existingToy.getPrice();

                // 4. Creăm un obiect Sales
                Sales sale = new Sales();
                sale.setToysName(existingToy.getName()); // Numele jucăriei
                sale.setCantity(toys.getCantity()); // Cantitatea vândută
                sale.setTransaction_date(new java.sql.Timestamp(System.currentTimeMillis())); // Data tranzacției
                sale.setTotal(total); // Totalul tranzacției

                // 5. Inserăm vânzarea în tabelul Sales
                session.save(sale);

                // 6. Actualizăm jucăria în tabelul Toys
                session.update(existingToy);

                // 7. Commit-uim tranzacția
                session.getTransaction().commit();
            } else {
                System.out.println("Cantitate insuficientă pentru vânzare.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Toys> search(String country, String name) {
        List<Toys> toysList = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Construim interogarea dinamic
            String hql = "FROM Toys t WHERE 1=1"; // 1=1 este o condiție mereu adevărată pentru a putea adăuga filtre condiționat

            if (country != null && !country.trim().isEmpty()) {
                hql += " AND LOWER(t.country) LIKE :country";
            }
            if (name != null && !name.trim().isEmpty()) {
                hql += " AND LOWER(t.name) LIKE :name";
            }
            hql += " AND t.cantity > 0"; // Adăugăm condiția quantity > 0

            Query<Toys> query = session.createQuery(hql, Toys.class);

            if (country != null && !country.trim().isEmpty()) {
                query.setParameter("country", "%" + country.toLowerCase() + "%");
            }
            if (name != null && !name.trim().isEmpty()) {
                query.setParameter("name", "%" + name.toLowerCase() + "%");
            }

            toysList = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toysList;
    }

    @Override
    public List<Toys> searchOos(String country, String name) {
        List<Toys> toysList = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Construim interogarea dinamic
            String hql = "FROM Toys t WHERE 1=1"; // 1=1 este o condiție mereu adevărată pentru a putea adăuga filtre condiționat

            if (country != null && !country.trim().isEmpty()) {
                hql += " AND LOWER(t.country) LIKE :country";
            }
            if (name != null && !name.trim().isEmpty()) {
                hql += " AND LOWER(t.name) LIKE :name";
            }
            hql += " AND t.cantity = 0"; // Adăugăm condiția quantity > 0

            Query<Toys> query = session.createQuery(hql, Toys.class);

            if (country != null && !country.trim().isEmpty()) {
                query.setParameter("country", "%" + country.toLowerCase() + "%");
            }
            if (name != null && !name.trim().isEmpty()) {
                query.setParameter("name", "%" + name.toLowerCase() + "%");
            }

            toysList = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toysList;
    }

    @Override
    public List<Toys> findAllOos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "Select t FROM Toys t where t.cantity = 0 order by t.id ";
            Query<Toys> query = session.createQuery(hql, Toys.class);
            return query.list();
        }
    }

    @Override
    public List<Toys> findAllAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "Select t FROM Toys t order by t.id ";
            Query<Toys> query = session.createQuery(hql, Toys.class);
            return query.list();
        }
    }

    @Override
    public Toys getToyById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Căutăm jucăria în baza de date folosind ID-ul
            return session.get(Toys.class, id); // Returnează obiectul Toys cu ID-ul specificat
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Dacă apare o eroare sau jucăria nu este găsită, returnează null
        }
    }

    @Override
    public List<Sales> getAll() {
        return List.of();
    }


}
