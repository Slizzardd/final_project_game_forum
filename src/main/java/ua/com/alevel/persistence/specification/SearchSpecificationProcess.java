package ua.com.alevel.persistence.specification;

import org.springframework.data.jpa.domain.Specification;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.entity.BaseEntity;
import ua.com.alevel.util.SpecificationUtil;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class SearchSpecificationProcess<E extends BaseEntity> implements SearchSpecification<E> {

    @Override
    public Specification<E> generateSpecification(DataTableRequest request, Class<E> entityClass) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = SpecificationUtil.generateSpecificationPredicates(request, entityClass, root, criteriaBuilder);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
