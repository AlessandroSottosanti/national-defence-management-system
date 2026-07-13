package it.application.nationaldefencemanagementsystem.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID matricola;
    private String modello;
    @Enumerated(EnumType.STRING)
    private VehicleStatus stato;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private VehicleCategory category;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Base base;

}
