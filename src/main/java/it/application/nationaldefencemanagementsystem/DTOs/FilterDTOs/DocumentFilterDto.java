package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentFilterDto {

    private Integer id;
    private String title;
    private String filePath;
    private Integer operatorId;
    private Integer vehicleId;
}