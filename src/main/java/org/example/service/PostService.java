package org.example.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.Part;
import jakarta.persistence.*;
import org.example.EntityManagerProvider;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.request.PostRequest;

public class PostService {

    private static final String PHOTO_UPLOAD_DIR = "uploads";
    private static PostService instance;

    private PostService() {}

    public static synchronized PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public void addPost(PostRequest postRequest, Part filePart) throws Exception {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            String photoFileName = null;
            if (filePart != null) {
                photoFileName = saveFile(filePart);
            }

            Post post = new Post(postRequest.getText(), photoFileName);
            entityManager.persist(post);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Error while adding post", e);
        } finally {
            entityManager.close();
        }
    }

    public List<Post> getAllPosts() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p", Post.class);
        List<Post> posts = query.getResultList();
        entityManager.close();
        return posts;
    }


    private String saveFile(Part filePart) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        String uploadPath = PHOTO_UPLOAD_DIR + File.separator + fileName;
        File file = new File(uploadPath);

        if (!file.exists()) {
            file.getParentFile().mkdirs(); // რომ დააპაროთ კატალოგი
            file.createNewFile();
        }

        filePart.write(uploadPath);
        return fileName;
    }

    public void deletePost(Long postId) throws Exception {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Post post = entityManager.find(Post.class, postId);
            if (post != null) {
                entityManager.remove(post);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Error while deleting post", e);
        } finally {
            entityManager.close();
        }
    }
    
    public void addComment(Long postId, Comment comment) throws Exception {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Post post = entityManager.find(Post.class, postId);
            if (post != null) {
                comment.setPost(post);
                entityManager.persist(comment);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Error while adding comment", e);
        } finally {
            entityManager.close();
        }
    }

    public Post getPostWithComments(Long postId) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        Post post = entityManager.find(Post.class, postId);
        if (post != null) {
            post.getComments().size(); // Lazy initialization
        }
        entityManager.close();
        return post;
    }
}
