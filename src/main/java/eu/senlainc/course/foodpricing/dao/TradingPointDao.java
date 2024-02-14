package eu.senlainc.course.foodpricing.dao;

import eu.senlainc.course.foodpricing.entities.TradingPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TradingPointDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_ALL_QUERY = "SELECT tp FROM TradingPoint tp";

    public List<TradingPoint> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, TradingPoint.class)
                .getResultList();
    }

    public Optional<TradingPoint> findById(Integer tradingPointId) {
        TradingPoint tradingPoint = entityManager.find(TradingPoint.class, tradingPointId);
        return Optional.ofNullable(tradingPoint);
    }
}