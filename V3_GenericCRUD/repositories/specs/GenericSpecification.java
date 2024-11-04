package ar.edu.utn.frc.tup.lciii.templateSpring.repositories.specs;

import ar.edu.utn.frc.tup.lciii.templateSpring.entities.base.BaseEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

/**
 * Specification to filter any entity by created date.
 * @param <E> entity that extends from base entity
 */
@NoArgsConstructor
@Component
public class GenericSpecification<E extends BaseEntity> {

    /**
     * Filter by date.
     * @param startDate start
     * @param endDate end
     * @return Specification with filtered dates.
     */
    public Specification<E> filterByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("createdDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * Filter by attributtes.
     * @param attributtes att for dinamyc search
     *  On key value u will put the name of the att and
     *  in the value the "value" that u want to search on
     *  that att.
     * @return Specification att.
     */
    public Specification<E> dynamicFilter(Map<String, Object> attributtes) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.conjunction();

            for (Map.Entry<String, Object> entry : attributtes.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null) {
                    predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get(key), value));
                }
            }

            return predicates;
        };
    }

    /**
     * Filter by attributtes.
     * @param value value for search.
     * @param entityFields the fields that you want to filter on
     * ur entity.
     * @return Specification att.
     */
    public Specification<E> valueDynamicFilter(String value, String... entityFields) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.conjunction();

            if (value != null) {
                String likePattern = "%" + value.toLowerCase(Locale.ROOT) + "%";
                Predicate orPredicates = criteriaBuilder.disjunction();

                for (String field : entityFields) {
                    orPredicates = criteriaBuilder.or(
                            orPredicates,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get(field).as(String.class)), likePattern)
                    );
                }

                predicates = criteriaBuilder.and(predicates, orPredicates);
            }

            return predicates;
        };
    }

    public <T extends Comparable<? super T>> Specification<E> filterBetween(T lower, T higher, String field) {
        return (root, query, criteriaBuilder) -> {
            T finalLower = lower;
            T finalHigher = higher;

            if (finalLower != null && finalHigher != null && finalHigher.compareTo(finalLower) < 0) {
                T temp = finalLower;
                finalLower = finalHigher;
                finalHigher = temp;
            }

            if (finalLower != null && finalHigher != null) {
                return criteriaBuilder.between(root.get(field), finalLower, finalHigher);
            } else if (finalLower != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(field), finalLower);
            } else if (finalHigher != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(field), finalHigher);
            }

            return criteriaBuilder.conjunction();
        };
    }


}
