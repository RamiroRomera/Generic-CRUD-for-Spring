package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;


import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDto;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPost;
import ar.edu.utn.frc.tup.lciii.templateSpring.dtos.DummyDtoPut;
import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import ar.edu.utn.frc.tup.lciii.templateSpring.repositories.DummyRepository;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.DummyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DummyServiceImpl extends GenericCRUDServiceImpl<DummyEntity, Long, DummyModel, DummyDtoPost, DummyDtoPut> {

    @Autowired
    private DummyRepository dummyRepository;

    @Override
    public JpaRepository<DummyEntity, Long> getRepository() {
        return dummyRepository;
    }

    @Override
    public String nameAttributeActive() {
        return "active";
    }
}
