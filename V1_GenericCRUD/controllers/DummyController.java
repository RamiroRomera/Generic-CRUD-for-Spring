package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDto;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPost;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPut;
import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.DummyService;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.impl.DummyServiceImpl;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.impl.GenericCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dummy")
public class DummyController extends GenericController<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut> {

    @Autowired
    private DummyServiceImpl dummyService;

    @Override
    public GenericCRUDService<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut> getService() {
        return dummyService;
    }
}
