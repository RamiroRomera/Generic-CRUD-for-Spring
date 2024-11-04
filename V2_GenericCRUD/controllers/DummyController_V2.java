package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPost;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPut;
import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.DummyService_V2;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService_V2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/dummmy/")
public class DummyController_V2 extends GenericController_V2<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut> {

    @Autowired
    private DummyService_V2 dummyService_V2;

    @Override
    public GenericCRUDService_V2 getService() {
        return dummyService_V2;
    }
}
