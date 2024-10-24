package ar.edu.utn.frc.tup.lciii.templateSpring.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public interface GenericCRUDService_V2<E, I, M, DTOPOST, DTOPUT> {
    ModelMapper getMapper();

    JpaRepository<E, I> getRepository();

    Class<E> entityClass();

    Class<M> modelClass();

    String nameAttributeActive();

    default M getById(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            return getMapper().map(entityOptional.get(), modelClass());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    default List<M> getAll() {
        List<E> entityList = getRepository().findAll();
        if (!entityList.isEmpty()) {
            return getMapper().map(entityList, new TypeToken<List<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    default Page<M> getAll(Pageable pageable) {
        Page<E> pageEntity = getRepository().findAll(pageable);
        if (!pageEntity.isEmpty()) {
            return getMapper().map(pageEntity, new TypeToken<Page<M>>(){}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    default M create(DTOPOST dtoPost) {
        E entityToSave = getMapper().map(dtoPost, entityClass());
        return getMapper().map(getRepository().save(entityToSave), modelClass());
    }

    default M update(DTOPUT dtoPut, I id) {
        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            getMapper().map(dtoPut, entity);
            return getMapper().map(getRepository().save(entity), modelClass());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    default M delete(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            Field field = ReflectionUtils.findField(entity.getClass(), nameAttributeActive());
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                    ReflectionUtils.setField(field, entity, false);
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, nameAttributeActive() + " field is not of type Boolean.");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity does not have the attribute " + nameAttributeActive() + ".");
            }
            return getMapper().map(getRepository().save(entity), modelClass());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }

    default M reactivate(I id) {
        Optional<E> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            Field field = ReflectionUtils.findField(entity.getClass(), nameAttributeActive());
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                    ReflectionUtils.setField(field, entity, true);
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, nameAttributeActive() + " field is not of type Boolean.");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity does not have the attribute " + nameAttributeActive() + ".");
            }
            return getMapper().map(getRepository().save(entity), modelClass());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }
}
