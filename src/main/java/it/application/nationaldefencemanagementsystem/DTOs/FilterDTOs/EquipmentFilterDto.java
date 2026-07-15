package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EquipmentFilterDto {

    private String name;
    private String model;
    private EquipmentCondition condition;
    private EquipmentStatus status;
    private Boolean fireArm;
    private Integer operatorId;
}