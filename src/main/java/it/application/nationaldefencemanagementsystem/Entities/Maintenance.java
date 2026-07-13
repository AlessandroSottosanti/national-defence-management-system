package it.application.nationaldefencemanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "maintenance", schema = "class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    @Column
    private Integer estimatedMaintenanceDays;
    @Column(precision = 12, scale = 2)
    private BigDecimal cost;

}
