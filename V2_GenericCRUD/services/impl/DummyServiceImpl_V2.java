package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.DummyEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.models.DummyModel;
import ar.edu.utn.frc.tup.lciii.templateSpring.repositories.DummyRepository;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.DummyService_V2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DummyServiceImpl_V2 implements DummyService_V2 {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DummyRepository dummyRepository;

    @Override
    public ModelMapper getMapper() {
        return modelMapper;
    }

    @Override
    public JpaRepository<DummyEntity, Long> getRepository() {
        return dummyRepository;
    }

    @Override
    public Class<DummyEntity> entityClass() {
        return DummyEntity.class;
    }

    @Override
    public Class<DummyModel> modelClass() {
        return DummyModel.class;
    }

    @Override
    public String nameAttributeActive() {
        return "active";
    }
}
