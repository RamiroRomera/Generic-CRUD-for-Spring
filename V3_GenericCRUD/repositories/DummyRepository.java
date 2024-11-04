package ar.edu.utn.frc.tup.lciii.templateSpring.repositories;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends GenericRepository<DummyEntity, Long> {
}
