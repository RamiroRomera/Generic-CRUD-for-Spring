package ar.edu.utn.frc.tup.lciii.templateSpring.entities;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "dummy")
public class DummyEntity extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String dummy;
}
