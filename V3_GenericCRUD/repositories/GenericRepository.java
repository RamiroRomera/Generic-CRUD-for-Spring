package ar.edu.utn.frc.tup.lciii.templateSpring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GenericRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
    Page<E> findAll(Specification<E> filter, Pageable pageable);
    List<E> findAll(Specification<E> filter);
}
