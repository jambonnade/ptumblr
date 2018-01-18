package de.jambonna.ptumblr.dao;

import de.jambonna.ptumblr.domain.Request;
import de.jambonna.ptumblr.domain.Request_;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;


@Repository
public class RequestDaoImpl implements RequestDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Request findLastRequest() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        CriteriaQuery<Request> cq = cb.createQuery(Request.class);
        Root<Request> root = cq.from(Request.class);
        cq.select(root);
        cq.orderBy(cb.desc(root.get(Request_.createdAt)));
        TypedQuery<Request> q = getEntityManager().createQuery(cq);
        q.setMaxResults(1);
        List<Request> results = q.getResultList();
        return results.size() > 0 ? results.get(0) : null;
    }

    @Override
    public void saveRequest(Request r) {
        getEntityManager().persist(r);
    }

}
