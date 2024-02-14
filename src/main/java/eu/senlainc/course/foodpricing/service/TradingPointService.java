package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.TradingPointDao;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.entities.TradingPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradingPointService {

    @Autowired
    private TradingPointDao tradingPointDao;
    private static final Logger logger = LoggerFactory.getLogger(TradingPointService.class);
    private static final String FETCHED_TRADING_POINTS_MESSAGE = "Fetched {} trading points";

    public List<TradingPointDto> getAllTradingPoints() {
        List<TradingPoint> tradingPoints = tradingPointDao.findAll();

        logger.info(FETCHED_TRADING_POINTS_MESSAGE, tradingPoints.size());

        return tradingPoints.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TradingPointDto mapToDto(TradingPoint tradingPoint) {
        return new TradingPointDto(
                tradingPoint.getPointId(),
                tradingPoint.getPointName(),
                tradingPoint.getAddress()
        );
    }

    public TradingPoint getTradingPointById(Integer tradingPointId) {
        return tradingPointDao.findById(tradingPointId).orElse(null);
    }
}