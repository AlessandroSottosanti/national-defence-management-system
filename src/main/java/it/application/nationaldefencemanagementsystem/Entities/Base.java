package it.application.nationaldefencemanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bases")
@Data
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

   /*
    @ManyToOne
    @JoinColumn(name = "armed_force_id", nullable = false)
    private ArmedForce armedForce;
    */
}