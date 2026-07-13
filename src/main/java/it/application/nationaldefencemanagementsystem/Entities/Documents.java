package it.application.nationaldefencemanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="documents")
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name="file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name="operator_id")
    private Operator operator;

    @ManyToOne
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle;

}
