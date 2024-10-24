package ar.edu.utn.frc.tup.lciii.templateSpring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    List<M> getAll();
    
    Page<M> getAll(Pageable pageable);

    M getById(I id);

    M create(DTOPOST dtoPost);

    M update(DTOPUT dtoPut, I id);

    M delete(I id);

    M reactivate(I id);
}
