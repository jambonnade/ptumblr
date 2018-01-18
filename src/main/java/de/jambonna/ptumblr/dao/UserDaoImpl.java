package de.jambonna.ptumblr.dao;

import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.Post_;
import de.jambonna.ptumblr.domain.User;
import de.jambonna.ptumblr.domain.User_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findUserById(Long id) {
        return getEntityManager().find(User.class, id);
    }

    @Override
    public User findUserByUsername(String username) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        cq.where(cb.equal(root.get(User_.username), username));
        cq.orderBy(cb.desc(root.get(User_.userId)));
        TypedQuery<User> q = getEntityManager().createQuery(cq);
        List<User> results = q.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        getEntityManager().persist(user);
    }

    @Override
    public List<Post> getPostsTo(User user, Long postId, Integer nb) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root);
        Predicate userFilter = cb.equal(root.get(Post_.user), user);
        if (postId != null) {
            cq.where(userFilter, cb.le(root.get(Post_.tumblrId), postId));
        } else {
            cq.where(userFilter);
        }
        cq.orderBy(cb.desc(root.get(Post_.tumblrId)));
        TypedQuery<Post> q = getEntityManager().createQuery(cq);
        q.setMaxResults(nb);
        List<Post> results = q.getResultList();
        
        return results;
    }

    @Override
    public List<Post> getPostsAfter(User user, Long postId, Integer nb) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root);
        cq.where(
            cb.and(
                cb.equal(root.get(Post_.user), user),
                cb.gt(root.get(Post_.tumblrId), postId)
            )
        );
        cq.orderBy(cb.asc(root.get(Post_.tumblrId)));
        TypedQuery<Post> q = getEntityManager().createQuery(cq);
        q.setMaxResults(nb);
        List<Post> results = q.getResultList();
        
        return results;
    }

    
    @Override
    public void savePosts(List<Post> posts) {
        for (Post p: posts) {
            getEntityManager().persist(p);
        }
    }


    @Override
    public void clearPosts(User user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        CriteriaDelete<Post> cq = cb.createCriteriaDelete(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.where(cb.equal(root.get(Post_.user), user));                
        getEntityManager().createQuery(cq).executeUpdate();
    }

    @Override
    public Post findPost(Long postId) {
        return getEntityManager().find(Post.class, postId);
    }

    
}
