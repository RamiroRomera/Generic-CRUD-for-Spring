package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public abstract class GenericCRUDServiceImpl<E, I, M, DTOPOST, DTOPUT> implements GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    @Autowired
    private ModelMapper modelMapper;

    public abstract JpaRepository<E, I> getRepository();

    public List<M> getAll() {
        List<E> entityList = getRepository().findAll();
        if (!entityList.isEmpty()) {
            return modelMapper.map(entityList, new TypeToken<List<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    public M getById(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            return modelMapper.map(entityOptional.get(), new TypeToken<List<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    public M create(DTOPOST dtoPost) {
        E entityToSave = modelMapper.map(dtoPost, new TypeToken<E>(){}.getType());
        E entityCreated = getRepository().save(entityToSave);
        return modelMapper.map(entityCreated, new TypeToken<M>(){}.getType());
    }

    public M update(DTOPUT dtoPut, I id) {
        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            E entityUpdated = getRepository().save(entity);
            return modelMapper.map(entityUpdated, new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    public M delete(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            setActiveStatus(entity, false);
            E deletedEntity = getRepository().save(entity);
            return modelMapper.map(reactivatedEntity, new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    public M reactivate(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            setActiveStatus(entity, true);
            E reactivatedEntity = getRepository().save(entity);
            return modelMapper.map(reactivatedEntity, new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    private void setActiveStatus(E entity, boolean isActive) {
        if (entity instanceof HasActiveStatus) {
            ((HasActiveStatus) entity).setIsActive(isActive);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity does not support active status management.");
        }
    }

    public interface HasActiveStatus {
        void setIsActive(boolean isActive);
    }
}
