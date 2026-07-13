package it.application.nationaldefencemanagementsystem.Entities;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Table(name="equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String model;
    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Column(name="fire_arm")
    private boolean fireArm;

    @Column(name = "ammunition_count")
    private int ammunitionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Operator operator;
}
