package it.application.nationaldefencemanagementsystem.DTOs;

import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class MaintenanceDto {

    private Integer id;
    private Vehicle vehicleId;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer estimatedMaintenanceDays;
    private BigDecimal cost;
}
