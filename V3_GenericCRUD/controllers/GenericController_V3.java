package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.base.BaseEntity;
import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService_V3;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;

public abstract class GenericController_V3<E extends BaseEntity, I, M, DTOPOST, DTOPUT, DTOFILTER> {

    public abstract GenericCRUDService_V3 getService();

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer size,
                                    DTOFILTER filter) {
         if (isFilterEmpty(filter)) {
            if (page != null && size != null) {
                return ResponseEntity.ok(getService().getAll(PageRequest.of(page, size)));
            } else {
                return ResponseEntity.ok(getService().getAll());
            }
        } else {
            if (page != null && size != null) {
                return ResponseEntity.ok(getService().getAll(PageRequest.of(page, size), filter));
            } else {
                return ResponseEntity.ok(getService().getAll(filter));
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<M> getById(@PathVariable I id) {
        M model = (M) getService().getById(id);
        return ResponseEntity.ok(model);
    }

    @PostMapping("")
    public ResponseEntity<M> create(@RequestBody @Valid DTOPOST dtoPost) {
        M createdEntity = (M) getService().create(dtoPost);
        return ResponseEntity.ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<M> update(@RequestBody @Valid DTOPUT dtoPut, @PathVariable I id) {
        M updatedEntity = (M) getService().update(dtoPut, id);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<M> delete(@PathVariable I id) {
        M deletedEntity = (M) getService().delete(id);
        return ResponseEntity.ok(deletedEntity);
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<M> reactivate(@PathVariable I id) {
        M reactivatedEntity = (M) getService().reactivate(id);
        return ResponseEntity.ok(reactivatedEntity);
    }

    private boolean isFilterEmpty(DTOFILTER filter) {
        if (filter == null) {
            return true;
        }

        Field[] fields = filter.getClass().getDeclaredFields();

        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);

            Object value = ReflectionUtils.getField(field, filter);

            if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
                return false;
            }
        }
        return true;
    }
}
