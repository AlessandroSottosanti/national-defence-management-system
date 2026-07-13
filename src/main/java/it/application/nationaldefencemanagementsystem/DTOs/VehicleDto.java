package it.application.nationaldefencemanagementsystem.DTOs;

import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Entities.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode


public class VehicleDto {
    private Integer id;
    private UUID matricola;
    private String modello;
    private VehicleStatus stato;
    private Integer vehicleCategoryId;
    private Integer baseId;

}
