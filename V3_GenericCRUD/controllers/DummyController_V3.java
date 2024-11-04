package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.annotations.GenericController;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoFilter;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPost;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPut;
import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.DummyService_V3;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService_V3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/dummy/")
public class DummyController_V3 extends GenericController_V3<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut, DummyDtoFilter> {
    @Autowired
    private DummyService_V3 dummyService_V3;

    @Override
    public GenericCRUDService_V3<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut, DummyDtoFilter> getService() {
        return dummyService_V3;
    }
}
