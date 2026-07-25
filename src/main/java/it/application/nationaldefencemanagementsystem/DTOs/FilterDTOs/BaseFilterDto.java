package it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BaseFilterDto {

    private String name;

    private String city;

    private String address;

    private Integer armedForceId;
}