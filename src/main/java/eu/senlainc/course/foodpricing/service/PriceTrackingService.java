package eu.senlainc.course.foodpricing.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import eu.senlainc.course.foodpricing.dao.PriceTrackingDao;
import eu.senlainc.course.foodpricing.dao.ProductDao;
import eu.senlainc.course.foodpricing.dto.PriceTrackingDto;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.entities.PriceTracking;
import eu.senlainc.course.foodpricing.entities.Product;
import eu.senlainc.course.foodpricing.entities.TradingPoint;
import eu.senlainc.course.foodpricing.expectations.BatchUploadException;
import eu.senlainc.course.foodpricing.expectations.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceTrackingService {

    @Autowired
    private PriceTrackingDao priceTrackingDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private TradingPointService tradingPointService;
    @Autowired
    private ProductDao productDao;
    private static final Logger logger = LoggerFactory.getLogger(PriceTrackingService.class);
    private static final String CSV_READ_ERROR_MESSAGE = "Error reading CSV file";
    private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Batch upload of price data completed successfully";
    private static final String BATCH_UPLOAD_ERROR_MESSAGE = "Error during batch upload";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    private static final String LINK_PRICE_SUCCESS_MESSAGE = "Linked price to product successfully. Tracking ID: {}";

    @Transactional
    public void batchUploadPriceData(MultipartFile file) {
        try {
            List<PriceTrackingDto> priceTrackingDtos = readPriceDataFromCsv(file);
            for (PriceTrackingDto priceTrackingDto : priceTrackingDtos) {
                PriceTracking priceTracking = convertDtoToEntity(priceTrackingDto);
                priceTrackingDao.linkPriceToProduct(priceTracking);
            }

            logger.info(BATCH_UPLOAD_SUCCESS_MESSAGE);
        } catch (IOException e) {
            logger.error(BATCH_UPLOAD_ERROR_MESSAGE, e);
            throw new BatchUploadException(BATCH_UPLOAD_ERROR_MESSAGE, e);
        }
    }

    private PriceTracking convertDtoToEntity(PriceTrackingDto priceTrackingDto) {
        Product product = new Product(
                priceTrackingDto.getProductId(),
                null,
                null
        );

        TradingPoint tradingPoint = new TradingPoint();
        tradingPoint.setPointId(priceTrackingDto.getTradingPoint());

        return new PriceTracking(
                null,
                product,
                tradingPoint,
                priceTrackingDto.getPrice(),
                priceTrackingDto.getStartDate(),
                priceTrackingDto.getEndDate()
        );
    }

    private List<PriceTrackingDto> readPriceDataFromCsv(MultipartFile file) throws IOException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = csvReader.readAll();
            return mapCsvToPriceTrackingDtoList(records);
        } catch (CsvException e) {
            throw new IOException(CSV_READ_ERROR_MESSAGE, e);
        }
    }

    private List<PriceTrackingDto> mapCsvToPriceTrackingDtoList(List<String[]> records) {
        List<PriceTrackingDto> priceTrackingDtos = new ArrayList<>();
        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);
            Product product = productService.getProductById(Integer.parseInt(record[0]));
            TradingPoint tradingPoint = tradingPointService.getTradingPointById(Integer.parseInt(record[1]));
            PriceTrackingDto priceTrackingDto = getPriceTrackingDto(record, product, tradingPoint);
            priceTrackingDtos.add(priceTrackingDto);
        }

        return priceTrackingDtos;
    }

    private static PriceTrackingDto getPriceTrackingDto(String[] record, Product productId, TradingPoint tradingPointId) {
        Double price = Double.parseDouble(record[2]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        LocalDate startDate = LocalDate.parse(record[3], formatter);
        LocalDate endDate = LocalDate.parse(record[4], formatter);

        Integer tradingPointIdValue = (tradingPointId != null) ? tradingPointId.getPointId() : null;
        Integer productIdValue = (productId != null) ? productId.getProductId() : null;

        return new PriceTrackingDto(
                tradingPointIdValue,
                productIdValue,
                tradingPointIdValue,
                price,
                startDate,
                endDate
        );
    }

    @Transactional
    public PriceTrackingDto linkPriceToProduct(PriceTrackingDto priceTrackingDto) {
        Integer productId = priceTrackingDto.getProductId();
        Integer tradingPointId = priceTrackingDto.getTradingPoint();

        Product product = productDao.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
        TradingPoint tradingPoint = tradingPointService.getTradingPointById(tradingPointId);

        PriceTracking priceTracking = new PriceTracking(
                null,
                product,
                tradingPoint,
                priceTrackingDto.getPrice(),
                LocalDate.now(),
                LocalDate.now()
        );

        priceTrackingDao.linkPriceToProduct(priceTracking);

        logger.info(LINK_PRICE_SUCCESS_MESSAGE, priceTracking.getTrackingId());

        return convertEntityToDto(priceTracking);
    }

    private PriceTrackingDto convertEntityToDto(PriceTracking priceTracking) {
        Product product = priceTracking.getProductId();
        TradingPoint tradingPoint = priceTracking.getTradingPoint();

        Integer productId = 0;
        if (product != null) {
            productId = product.getProductId();
        }

        Integer tradingPointId = 0;
        if (tradingPoint != null) {
            tradingPointId = tradingPoint.getPointId();
        }

        return new PriceTrackingDto(
                priceTracking.getTrackingId(),
                productId,
                tradingPointId,
                priceTracking.getPrice(),
                priceTracking.getStartDate(),
                priceTracking.getEndDate()
        );
    }

    public List<PriceTrackingDto> getPriceDynamicsForProduct(Integer productId, LocalDate startDate, LocalDate endDate) {
        return priceTrackingDao.getPriceDynamicsForProduct(productId, startDate, endDate);
    }

    public List<PriceTrackingDto> comparePrices(Integer productId) {
        return priceTrackingDao.comparePrices(productId);
    }

    public List<TradingPointDto> getPriceGraphData(Integer productId) {
        return priceTrackingDao.getPriceGraphData(productId);
    }
}