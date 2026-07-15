package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MaintenanceFilterDto {

    private Integer vehicleId;
    private Integer equipmentId;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer estimatedMaintenanceDays;

    private BigDecimal cost;
}