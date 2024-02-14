package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.PriceTrackingDao;
import eu.senlainc.course.foodpricing.dao.ProductDao;
import eu.senlainc.course.foodpricing.dto.PriceTrackingDto;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.entities.Product;
import eu.senlainc.course.foodpricing.entities.TradingPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PriceTrackingServiceTest {

    @Mock
    private PriceTrackingDao priceTrackingDao;
    @Mock
    private ProductService productService;
    @Mock
    private TradingPointService tradingPointService;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private PriceTrackingService priceTrackingService;

    private static PriceTrackingDto createPriceTrackingDto(Integer trackingId, Integer productId, Integer tradingPoint, Double price, LocalDate startDate, LocalDate endDate) {
        return new PriceTrackingDto(trackingId, productId, tradingPoint, price, startDate, endDate);
    }

    private static final PriceTrackingDto PRICE_TRACKING_DTO = createPriceTrackingDto(null, 1, 2, 20.0, LocalDate.of(2023, 8, 5),
            LocalDate.of(2023, 8, 10)
    );
    private static final PriceTrackingDto PRICE_TRACKING_DTO_1 = createPriceTrackingDto(1, 1, 1, 20.0, LocalDate.of(2023, 8, 5),
            LocalDate.of(2023, 8, 10));
    private static final PriceTrackingDto PRICE_TRACKING_DTO_2 = createPriceTrackingDto(2, 2, 2, 30.0, LocalDate.of(2023, 9, 10),
            LocalDate.of(2023, 9, 15));
    private static final String CSV_CONTENT = """
        product_id,point_id,price,start_date,end_date
        1,1,20.00,2023-08-05,2023-08-05
        2,2,55.00,2023-10-07,2023-10-07
        3,3,25.00,2023-09-07,2023-09-07
        """;
    private static final Integer PRODUCT_ID = 1;
    private static final LocalDate START_DATE = LocalDate.of(2023, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2023, 12, 31);
    private static final String TRADING_POINT = "SuperMart";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBatchUploadPriceData_SuccessfulUpload() throws IOException {
        MultipartFile mockedFile = mock(MultipartFile.class);
        when(mockedFile.getInputStream()).thenReturn(new ByteArrayInputStream(CSV_CONTENT.getBytes()));

        priceTrackingService.batchUploadPriceData(mockedFile);

        verify(priceTrackingDao, times(3)).linkPriceToProduct(any());
        verify(productService, times(3)).getProductById(anyInt());
        verify(tradingPointService, times(3)).getTradingPointById(anyInt());
    }

    @Test
    void testLinkPriceToProduct_SuccessfulLinking() {
        when(productDao.findById(anyInt())).thenReturn(Optional.of(new Product()));
        when(tradingPointService.getTradingPointById(anyInt())).thenReturn(new TradingPoint());

        PriceTrackingDto resultDto = priceTrackingService.linkPriceToProduct(PRICE_TRACKING_DTO);

        verify(priceTrackingDao, times(1)).linkPriceToProduct(any());

        assertNotNull(resultDto);
    }

    @Test
    void testGetPriceDynamicsForProduct_ReturnsPriceDynamics() {
        when(priceTrackingDao.getPriceDynamicsForProduct(PRODUCT_ID, START_DATE, END_DATE)).thenReturn(Arrays.asList(PRICE_TRACKING_DTO_1, PRICE_TRACKING_DTO_2));

        List<PriceTrackingDto> priceDynamics = priceTrackingService.getPriceDynamicsForProduct(PRODUCT_ID, START_DATE, END_DATE);

        verify(priceTrackingDao, times(1)).getPriceDynamicsForProduct(PRODUCT_ID, START_DATE, END_DATE);

        assertEquals(2, priceDynamics.size());
        assertEquals(PRICE_TRACKING_DTO_1, priceDynamics.get(0));
        assertEquals(PRICE_TRACKING_DTO_2, priceDynamics.get(1));
    }

    @Test
    void testComparePrices_ReturnsPriceComparison() {
        when(priceTrackingDao.comparePrices(PRODUCT_ID)).thenReturn(Arrays.asList(PRICE_TRACKING_DTO_1, PRICE_TRACKING_DTO_2));

        List<PriceTrackingDto> priceComparison = priceTrackingService.comparePrices(PRODUCT_ID);

        verify(priceTrackingDao, times(1)).comparePrices(PRODUCT_ID);

        assertEquals(2, priceComparison.size());
        assertEquals(PRICE_TRACKING_DTO_1, priceComparison.get(0));
        assertEquals(PRICE_TRACKING_DTO_2, priceComparison.get(1));
    }

    @Test
    void testGetPriceGraphData_ReturnsPriceGraphData() {
        when(priceTrackingDao.getPriceGraphData(PRODUCT_ID)).thenReturn(Arrays.asList(createTradingPointDto(1), createTradingPointDto(2)));

        List<TradingPointDto> priceGraphData = priceTrackingService.getPriceGraphData(PRODUCT_ID);

        verify(priceTrackingDao, times(1)).getPriceGraphData(PRODUCT_ID);

        assertEquals(2, priceGraphData.size());
        assertEquals(createTradingPointDto(1), priceGraphData.get(0));
        assertEquals(createTradingPointDto(2), priceGraphData.get(1));
    }

    private TradingPointDto createTradingPointDto(Integer tradingPointId) {
        return new TradingPointDto(tradingPointId, TRADING_POINT + tradingPointId, null);
    }
}