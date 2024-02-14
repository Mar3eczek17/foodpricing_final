package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.TradingPointDao;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.entities.TradingPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TradingPointServiceTest {

    @Mock
    private TradingPointDao tradingPointDao;

    @InjectMocks
    private TradingPointService tradingPointService;

    private static final TradingPoint TRADING_POINT_1 = new TradingPoint(1, "Point1", "Address1");
    private static final TradingPoint TRADING_POINT_2 = new TradingPoint(2, "Point2", "Address2");
    private static final Integer TRADING_POINT_ID = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTradingPoints_ReturnsExpectedListSize() {
        List<TradingPoint> expectedTradingPoints = Arrays.asList(TRADING_POINT_1, TRADING_POINT_2);
        when(tradingPointDao.findAll()).thenReturn(expectedTradingPoints);

        List<TradingPointDto> actualTradingPoints = tradingPointService.getAllTradingPoints();

        verify(tradingPointDao, times(1)).findAll();

        assertEquals(expectedTradingPoints.size(), actualTradingPoints.size());
    }

    @Test
    void testGetTradingPointById_ReturnsExpectedTradingPoint() {
        when(tradingPointDao.findById(TRADING_POINT_ID)).thenReturn(java.util.Optional.of(TRADING_POINT_1));

        TradingPoint tradingPoint = tradingPointService.getTradingPointById(TRADING_POINT_ID);

        verify(tradingPointDao, times(1)).findById(TRADING_POINT_ID);

        assertNotNull(tradingPoint);
        assertEquals(TRADING_POINT_1.getPointId(), tradingPoint.getPointId());
        assertEquals(TRADING_POINT_1.getPointName(), tradingPoint.getPointName());
        assertEquals(TRADING_POINT_1.getAddress(), tradingPoint.getAddress());
    }
}
