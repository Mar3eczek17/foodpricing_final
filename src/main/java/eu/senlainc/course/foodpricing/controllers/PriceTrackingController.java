package eu.senlainc.course.foodpricing.controllers;

import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.PriceTrackingDto;
import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.service.PriceTrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/price-tracking")
public class PriceTrackingController {

    @Autowired
    private PriceTrackingService priceTrackingService;

    private static final String SELECT_FILE_MESSAGE = "Please select a file.";

    private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Batch upload successful";

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/link-price")
    public ResponseEntity<PriceTrackingDto> linkPriceToProduct(@RequestBody @Valid PriceTrackingDto priceTrackingDto) {
        PriceTrackingDto linkedPrice = priceTrackingService.linkPriceToProduct(priceTrackingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkedPrice);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/dynamics")
    public ResponseEntity<List<PriceTrackingDto>> getPriceDynamicsForProduct(@RequestBody @Valid PriceTrackingDto trackingDto) {
        List<PriceTrackingDto> priceDynamics = priceTrackingService.getPriceDynamicsForProduct(
                trackingDto.getProductId(), trackingDto.getStartDate(), trackingDto.getEndDate());
        return ResponseEntity.ok(priceDynamics);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/compare-prices/{productId}")
    public ResponseEntity<List<PriceTrackingDto>> comparePrices(@PathVariable Integer productId) {
        List<PriceTrackingDto> priceComparison = priceTrackingService.comparePrices(productId);
        return ResponseEntity.ok(priceComparison);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/price-graph/{productId}")
    public ResponseEntity<List<TradingPointDto>> getPriceGraphData(@PathVariable Integer productId) {
        List<TradingPointDto> priceGraphData = priceTrackingService.getPriceGraphData(productId);
        return ResponseEntity.ok(priceGraphData);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch-upload")
    public ResponseEntity<ApiResponse> batchUploadPriceData(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            ApiResponse response = new ApiResponse(SELECT_FILE_MESSAGE);
            return ResponseEntity.badRequest().body(response);
        }

        priceTrackingService.batchUploadPriceData(file);
        ApiResponse response = new ApiResponse(BATCH_UPLOAD_SUCCESS_MESSAGE);
        return ResponseEntity.ok(response);
    }
}