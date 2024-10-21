package ar.edu.utn.frc.tup.lciii.templateSpring.services.impl;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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
public abstract class GenericCRUDServiceImpl<E, I, M, DTOPOST, DTOPUT> implements GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * These attributes are used for mapping on {@link ModelMapper}
     */
    private final Class<E> entityClass;
    private final Class<M> modelClass;

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

    /**
     * Constructor to obtain the concrete classes for {@link ModelMapper}
     */
    @SuppressWarnings("unchecked")
    public GenericCRUDServiceImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
        this.modelClass = (Class<M>) genericSuperclass.getActualTypeArguments()[2];
    }

    public M getById(I id) {
        Optional<E> plotEntityOptional = getRepository().findById(id);
        if (plotEntityOptional.isPresent()) {
            return modelMapper.map(plotEntityOptional.get(), modelClass);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    public List<M> getAll() {
        List<E> entityList = getRepository().findAll();
        if (!entityList.isEmpty()) {
            return modelMapper.map(entityList, new TypeToken<List<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    public Page<M> getAll(Pageable pageable) {
        Page<E> pageEntity = getRepository().findAll(pageable);
        if (!pageEntity.isEmpty()) {
            return modelMapper.map(pageEntity, new TypeToken<Page<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    public M create(DTOPOST dtoPost) {
        E entityToSave = modelMapper.map(dtoPost, entityClass);
        return modelMapper.map(getRepository().save(entityToSave), modelClass);
    }

    public M update(DTOPUT dtoPut, I id) {
        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            modelMapper.map(dtoPut, entity);
            return modelMapper.map(getRepository().save(entity), modelClass);
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
            return modelMapper.map(getRepository().save(entity), modelClass);
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
