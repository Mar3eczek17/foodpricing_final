package eu.senlainc.course.foodpricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradingPointDto {

    private Integer pointId;

    private String pointName;

    private String address;
}