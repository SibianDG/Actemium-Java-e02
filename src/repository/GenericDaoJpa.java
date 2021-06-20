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

    // Mehtod for debugging and testing untill solution is found
    @Override
    public Collection<T> findAll() {
    	System.out.printf("select entity from " + type.getName() + " entity", type);
    	System.out.println();
    	System.out.printf(type.getSimpleName()+".findAll", type);
    	System.out.println();
		if (type.getSimpleName().equals("ActemiumTicket")) {
			System.out.println("named query");
//			em.refresh();
			em.clear();
			return em.createNamedQuery(type.getSimpleName() + ".findAll", type).getResultList();
		} else {
			em.clear();
			em.clear();
			return em.createQuery("select entity from " + type.getName() + " entity", type).getResultList();
		}
    }
    
    // TODO - once problem is fixed replace above method with the one below
//    @Override
//    public Collection<T> findAll() {
//		return em.createQuery("select entity from " + type.getName() + " entity", type).getResultList();		
//    }

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
