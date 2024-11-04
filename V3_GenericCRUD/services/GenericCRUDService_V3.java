package ar.edu.utn.frc.tup.lciii.templateSpring.services;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.base.BaseEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.repositories.GenericRepository;
import ar.edu.utn.frc.tup.lciii.templateSpring.repositories.specs.GenericSpecification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericCRUDService_V3<E extends BaseEntity, I, M, DTOPOST, DTOPUT, DTOFILTER> {
    ModelMapper getMapper();

    GenericRepository<E, I> getRepository();

    GenericSpecification<E> getSpecification();

    Class<E> entityClass();

    Class<M> modelClass();

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
            return getMapper().map(entityList, new TypeToken<List<M>>() {}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    default List<M> getAll(DTOFILTER filter) {
        Specification<E> spec = getFilterMap(filter);
        List<E> entityList = getRepository().findAll(spec);
        if (!entityList.isEmpty()) {
            return getMapper().map(entityList, new TypeToken<List<M>>() {}.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    default Page<M> getAll(Pageable pageable) {
        Page<E> pageEntity = getRepository().findAll(pageable);
        if (!pageEntity.isEmpty()) {
            return getMapper().map(pageEntity, new TypeToken<Page<M>>() {
            }.getType());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content retrieved.");
        }
    }

    default Page<M> getAll(Pageable pageable, DTOFILTER filter) {
        Specification<E> spec = getFilterMap(filter);
        Page<E> pageEntity = getRepository().findAll(spec, pageable);
        if (!pageEntity.isEmpty()) {
            return getMapper().map(pageEntity, new TypeToken<Page<M>>() {
            }.getType());
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
        return changeActiveStatus(id, false);
    }

    default M reactivate(I id) {
        return changeActiveStatus(id, true);
    }

    private Specification<E> getFilterMap(DTOFILTER filter) {
        Map<String, Object> filterMap = new HashMap<>();

        ReflectionUtils.doWithFields(filter.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(filter);
            if (value != null) {
                filterMap.put(field.getName(), value);
            }
        });

        return getSpecification().dynamicFilter(filterMap);
    }

    private M changeActiveStatus(I id, boolean isActive) {
        Optional<E> entityOptional = getRepository().findById(id);

        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();
            entity.setIsActive(isActive);
            return getMapper().map(getRepository().save(entity), modelClass());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found any object with id: " + id);
        }
    }
}
