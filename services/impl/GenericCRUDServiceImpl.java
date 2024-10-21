package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

/**
 * Generic CRUD :)
 * @param <E> represent your entity
 * @param <I> represent the type of ID on the entity
 * @param <M> represent the model to return on methods
 * @param <DTOPOST> represent the object to create an entity
 * @param <DTOPUT> represent the object to update an entity
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public abstract class GenericCRUDServiceImpl<E, I, M, DTOPOST, DTOPUT> implements GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * When you implement this class will replace this method
     * with your repository.
     * @return {@link JpaRepository}
     */
    public abstract JpaRepository<E, I> getRepository();

    /**
     * This will be used to implement logical delete and
     * reactivation of your entity.
     * @return {@link String} representing the name of your attribute.
     */
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
        return modelMapper.map(getRepository().save(entityToSave), new TypeToken<M>(){}.getType());
    }

    public M update(DTOPUT dtoPut, I id) {
        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            modelMapper.map(dtoPut, entity);
            return modelMapper.map(getRepository().save(entity), new TypeToken<M>(){}.getType());
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


    /**
     * This function is used for the implementation of reactivate and delete.
     * @param id represent the id of the object to update.
     * @param isActive represent the new state.
     * @return {@link M} the model updated.
     */
    private M changeActiveStatus(I id, boolean isActive) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            setActiveStatus(entity, isActive);
            return modelMapper.map(getRepository().save(entity), new TypeToken<M>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    /**
     * Will search the attribute for logical delete and change it.
     * @param entity represent the object that will change.
     * @param isActive represent the new status of the object.
     */
    private void setActiveStatus(E entity, boolean isActive) {
        Field field = ReflectionUtils.findField(entity.getClass(), nameAttributeActive());
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                ReflectionUtils.setField(field, entity, isActive);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, nameAttributeActive() + " field is not of type Boolean.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity does not have the attribute " + nameAttributeActive() + ".");
        }
    }
}
