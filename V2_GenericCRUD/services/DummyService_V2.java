package ar.edu.utn.frc.tup.lciii.templateSpring.services;

import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPost;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPut;
import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import org.springframework.stereotype.Service;

@Service
public interface DummyService_V2 extends GenericCRUDService_V2<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut> {

}
