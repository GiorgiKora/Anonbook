package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.Comment;

import jakarta.persistence.*;
import java.util.List;

public class CommentService {

    private static final CommentService instance = new CommentService();
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AnonbookPU");

    private CommentService() {}

    public static CommentService getInstance() {
        return instance;
    }

    public void save(Comment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(comment);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Object> getCommentsByPostId(Long postId) {
        EntityManager em = emf.createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery();
            Root<Comment> root = cq.from(Comment.class);
            cq.select(root).where(cb.equal(root.get("postId"), postId));
            ((CriteriaQuery<?>) cq).orderBy(cb.asc(root.get("timestamp")));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public void close() {
        emf.close();
    }
}
