package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService_V2;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class GenericController_V2<E, I, M, DTOPOST, DTOPUT> {

    public abstract GenericCRUDService_V2 getService();

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(getService().getAll(PageRequest.of(page, size)));
        } else {
            return ResponseEntity.ok(getService().getAll());
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
}
