package eu.senlainc.course.foodpricing.dao;

import eu.senlainc.course.foodpricing.dto.PriceTrackingDto;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.entities.PriceTracking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PriceTrackingDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_PRICE_DYNAMICS_QUERY =
            "SELECT pt FROM PriceTracking pt " +
                    "WHERE pt.productId.productId = :productId " +
                    "AND pt.startDate >= :startDate " +
                    "AND pt.endDate <= :endDate " +
                    "ORDER BY pt.startDate";

    private static final String COMPARE_PRICES_QUERY =
            "SELECT pt FROM PriceTracking pt " +
                    "WHERE pt.productId.productId = :productId " +
                    "ORDER BY pt.tradingPoint.pointId";

    private static final String GET_PRICE_GRAPH_DATA_QUERY =
            "SELECT new TradingPointDto(" +
                    "pt.tradingPoint.pointId, pt.tradingPoint.pointName, pt.tradingPoint.address) " +
                    "FROM PriceTracking pt " +
                    "WHERE pt.productId.productId = :productId " +
                    "ORDER BY pt.startDate";

    public void linkPriceToProduct(PriceTracking priceTracking) {
        entityManager.persist(priceTracking);
    }

    public List<PriceTrackingDto> getPriceDynamicsForProduct(Integer productId, LocalDate startDate, LocalDate endDate) {
        return entityManager.createQuery(GET_PRICE_DYNAMICS_QUERY, PriceTrackingDto.class)
                .setParameter("productId", productId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<PriceTrackingDto> comparePrices(Integer productId) {
        return entityManager.createQuery(COMPARE_PRICES_QUERY, PriceTrackingDto.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    public List<TradingPointDto> getPriceGraphData(Integer productId) {
        return entityManager.createQuery(GET_PRICE_GRAPH_DATA_QUERY, TradingPointDto.class)
                .setParameter("productId", productId)
                .getResultList();
    }
}