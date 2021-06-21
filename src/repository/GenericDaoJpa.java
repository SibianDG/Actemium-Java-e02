package repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GenericDaoJpa<T> implements GenericDao<T>, AutoCloseable{
	
    private static final String PU_NAME = "ticketlocal1";
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> type;
    
    public GenericDaoJpa(Class<T> type) {
        this.type = type;
    }
    @Override
    public void closePersistency() {
        em.close();
        emf.close();
    }
    @Override
    public Class<? extends Throwable> startTransaction() {
        em.getTransaction().begin();
        return null;
    }
    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }
    @Override
    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public Collection<T> findAll() {
		em.clear(); // em cache needs to be cleared for refresh data
		return em.createQuery("select entity from " + type.getName() + " entity", type).getResultList();		
    }

    @Override
    public <U> T get(U id) {
        T entity = em.find(type, id);
        return entity;
    }

    @Override
    public T update(T object) {
        return em.merge(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
    }

    @Override
    public void insert(T object) {
        em.persist(object);
    }

    @Override
    public <U> boolean exists(U id) {
        T entity = em.find(type, id);
        return entity != null;
    }

    @Override
    public void close() {
        closePersistency();
    }
}
