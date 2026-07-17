package it.application.nationaldefencemanagementsystem.DTOs;

import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentsDto {

    private Integer id;
    private String title;
    private String filePath;
    private Integer operatorId;
    private Integer vehicleId;
}
