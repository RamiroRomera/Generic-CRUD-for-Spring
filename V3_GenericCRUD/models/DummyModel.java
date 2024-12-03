package ar.edu.utn.frc.tup.lciii.templateSpring.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@NoArgsConstructor
public class DummyModel {
    private Long id;
    private String dummy;
    private Boolean isActive;
    private LocalDateTime lastModifiedDate;
    private LocalDateTime createdDate;
}
