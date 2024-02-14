package eu.senlainc.course.foodpricing.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "trading_point")
@Data
@AllArgsConstructor
public class TradingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Integer pointId;

    @Column(name = "point_name")
    private String pointName;

    @Column(name = "address")
    private String address;

    public TradingPoint() {
    }
}