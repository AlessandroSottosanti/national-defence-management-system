package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import it.application.nationaldefencemanagementsystem.Entities.VehicleStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class VehicleFilterDto {

    private UUID matricola;
    private String modello;
    private VehicleStatus stato;
    private Integer vehicleCategoryId;
    private Integer baseId;
}