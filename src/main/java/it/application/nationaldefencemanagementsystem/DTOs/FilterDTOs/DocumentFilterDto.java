package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentFilterDto {

    private String title;
    private Integer operatorId;
    private String operatorFirstName;
    private String operatorLastName;
    private String operatorServiceNumber;
    private Integer vehicleId;
}