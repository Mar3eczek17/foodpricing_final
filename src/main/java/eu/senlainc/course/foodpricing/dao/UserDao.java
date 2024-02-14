package eu.senlainc.course.foodpricing.dao;

import eu.senlainc.course.foodpricing.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String EXISTS_BY_USERNAME_QUERY = "SELECT COUNT(u) FROM User u WHERE u.username = :username";

    private static final String FIND_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = :username";

    private static final String FIND_BY_EMAIL_QUERY = "SELECT u FROM User u WHERE u.email = :email";

    public Optional<User> findUserById(Integer userId) {
        User user = entityManager.find(User.class, userId);
        return Optional.ofNullable(user);
    }

    public void save(User user) {
        if (user.getUserId() == 0) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery(EXISTS_BY_USERNAME_QUERY, Long.class);
        query.setParameter("username", username);

        Long count = query.getSingleResult();
        return count > 0;
    }

    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(FIND_BY_USERNAME_QUERY, User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(FIND_BY_EMAIL_QUERY, User.class);
        query.setParameter("email", email);

        List<User> users = query.getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}