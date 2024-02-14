package eu.senlainc.course.foodpricing.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PriceTrackingDto {

    private Integer trackingId;

    @NotNull(message = "ProductId cannot be null")
    @Positive(message = "ProductId must be a positive integer")
    private Integer productId;

    private Integer tradingPoint;

    private Double price;

    @PastOrPresent(message = "StartDate must be in the past or present")
    private LocalDate startDate;

    @PastOrPresent(message = "EndDate must be in the past or present")
    private LocalDate endDate;
}