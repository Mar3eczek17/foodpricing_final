package eu.senlainc.course.foodpricing.dao;

import eu.senlainc.course.foodpricing.entities.Product;
import eu.senlainc.course.foodpricing.entities.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_PRODUCTS_BY_CATEGORY_QUERY =
            "SELECT p FROM Product p WHERE p.categoryId = :category";

    public void save(Product product) {
        entityManager.merge(product);
    }

    public Optional<Product> findById(Integer productId) {
        Product product = entityManager.find(Product.class, productId);
        return Optional.ofNullable(product);
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        TypedQuery<Product> query = entityManager.createQuery(GET_PRODUCTS_BY_CATEGORY_QUERY, Product.class);
        query.setParameter("category", category);

        return query.getResultList();
    }

    public void delete(Product existingProduct) {
        entityManager.remove(existingProduct);
    }
}