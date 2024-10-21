package ar.edu.utn.frc.tup.lciii.templateSpring.services;

import java.util.List;

public interface GenericCRUDService<E, I, M, DTOPOST, DTOPUT> {

    List<M> getAll();

    M getById(I id);

    M create(DTOPOST dtoPost);

    M update(DTOPUT dtoPut, I id);

    M delete(I id);

    M reactivate(I id);

    interface HasActiveStatus {
        void setIsActive(boolean isActive);
    }
}
