package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OperatorFilterDto {

    private String rank;
    private String serviceNumber;
    private String firstName;
    private String lastName;
    private Integer minHeightInCm;
    private Integer maxHeightInCm;
    private Integer minWeightInKg;
    private Integer maxWeightInKg;
    private Integer baseId;
    private OperatorStatus status;
}