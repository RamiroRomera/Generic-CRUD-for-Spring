package ar.edu.utn.frc.tup.lciii.templateSpring.controllers;

import ar.edu.utn.frc.tup.lciii.templateSpring.services.GenericCRUDService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<E, I, M, DTOPOST, DTOPUT> {

    public abstract GenericCRUDService<E, I, M, DTOPOST, DTOPUT> getService();

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
        return ResponseEntity.ok(getService().getById(id));
    }

    @PostMapping("")
    public ResponseEntity<M> create(@RequestBody DTOPOST dtoPost) {
        return ResponseEntity.ok(getService().create(dtoPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<M> update(@RequestBody DTOPUT dtoPut, @PathVariable I id) {
        return ResponseEntity.ok(getService().update(dtoPut, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<M> delete(@PathVariable I id) {
        return ResponseEntity.ok(getService().delete(id));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<M> reactivate(@PathVariable I id) {
        return ResponseEntity.ok(getService().reactivate(id));
    }
}
