package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public abstract class GenericCRUDServiceImpl<E, I, M, DTOPOST, DTOPUT> implements GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    @Autowired
    private ModelMapper modelMapper;

    public abstract JpaRepository<E, I> getRepository();

    public abstract String nameAttributeActive();

    public List<M> getAll() {
        List<E> entityList = getRepository().findAll();
        if (!entityList.isEmpty()) {
            return modelMapper.map(entityList, new TypeToken<List<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    public M getById(I id) {
        Optional<E> plotEntityOptional = getRepository().findById(id);
        if (plotEntityOptional.isPresent()) {
            return modelMapper.map(plotEntityOptional.get(), new TypeToken<List<M>>(){}.getType());
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
            modelMapper.map(entity, dtoPut);
            E entityUpdated = getRepository().save(entity);
            return modelMapper.map(entityUpdated, new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    public M delete(I id) {
        return changeActiveStatus(id, false);
    }

    public M reactivate(I id) {
        return changeActiveStatus(id, true);
    }

    private M changeActiveStatus(I id, boolean isActive) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();

            setActiveStatus(entity, isActive);

            E updatedEntity = getRepository().save(entity);
            return modelMapper.map(updatedEntity, new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    private void setActiveStatus(E entity, boolean isActive) {
        Field field = ReflectionUtils.findField(entity.getClass(), nameAttributeActive());

        if (field != null) {
            ReflectionUtils.makeAccessible(field);

            if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                ReflectionUtils.setField(field, entity, isActive);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Active status field is not of type Boolean.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity does not support active status management.");
        }
    }
}
