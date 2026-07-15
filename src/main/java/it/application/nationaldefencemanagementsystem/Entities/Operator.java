package it.application.nationaldefencemanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operators")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String serviceNumber;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String rank;

    @Column(nullable = false)
    private Integer heightInCm;

    @Column(nullable = false)
    private Integer weightInKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperatorStatus status;

    @ManyToOne
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;

}
