package it.application.nationaldefencemanagementsystem.DTOs;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OperatorDto {
    private Integer id;
    private String serviceNumber;
    private String firstName;
    private String lastName;
    private String rank;
    private OperatorStatus status;
    private Integer baseId;
}