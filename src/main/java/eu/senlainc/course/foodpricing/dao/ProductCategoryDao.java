package eu.senlainc.course.foodpricing.dao;

import eu.senlainc.course.foodpricing.entities.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_ALL_QUERY = "SELECT pc FROM ProductCategory pc";

    public List<ProductCategory> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, ProductCategory.class)
                .getResultList();
    }
}