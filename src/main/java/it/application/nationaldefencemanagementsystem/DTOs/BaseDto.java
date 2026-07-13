package it.application.nationaldefencemanagementsystem.DTOs;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BaseDto {

    private Integer id;

    private String name;

    private String city;

    private String address;

    private Integer armedForceId;
}