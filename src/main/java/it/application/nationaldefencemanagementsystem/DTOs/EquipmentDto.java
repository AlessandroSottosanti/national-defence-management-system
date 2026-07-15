package it.application.nationaldefencemanagementsystem.DTOs;

import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EquipmentDto {
    private Integer id;
    private String name;
    private String model;
    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    private boolean fireArm;
    private String ammunitionType;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;
    private Integer operatorId;
}
