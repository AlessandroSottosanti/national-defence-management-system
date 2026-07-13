package it.application.nationaldefencemanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "armed_forces")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArmedForce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}