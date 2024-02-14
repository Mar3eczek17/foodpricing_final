package eu.senlainc.course.foodpricing.controllers;

import eu.senlainc.course.foodpricing.dto.TradingPointDto;
import eu.senlainc.course.foodpricing.service.TradingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trading-points")
public class TradingPointController {

    @Autowired
    private TradingPointService tradingPointService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<TradingPointDto>> getAllTradingPoints() {
        List<TradingPointDto> tradingPoints = tradingPointService.getAllTradingPoints();
        return ResponseEntity.ok(tradingPoints);
    }
}