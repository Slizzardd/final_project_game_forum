package ua.com.alevel.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.entity.BaseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class SpecificationUtil {

    private SpecificationUtil() {
    }

    public static <E extends BaseEntity> List<Predicate> generateSpecificationPredicates(
            DataTableRequest request,
            Class<E> entityClass,
            Root<E> root,
            CriteriaBuilder criteriaBuilder
    ) {
        System.out.println("SpecificationUtil.generateSpecificationPredicates");
        List<Predicate> predicates = new ArrayList<>();

        Field[] fields = FieldUtils.getAllFields(entityClass);
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers()) &&
                    !Modifier.isStatic(field.getModifiers()) &&
                    !Modifier.isTransient(field.getModifiers())) {
                String[] params = request.getRequestParamMap().get(field.getName());
                if (ArrayUtils.isNotEmpty(params)) {
                    if (field.getType().isAssignableFrom(String.class)) {
                        List<Predicate> innerPredicates = new ArrayList<>();
                        for (String param : params) {
                            if (param.contains("%")) {
                                innerPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(field.getName())), param));
                            }
                        }
                        if (CollectionUtils.isNotEmpty(innerPredicates)) {
                            predicates.add(criteriaBuilder.or(innerPredicates.toArray(new Predicate[0])));
                        } else {
                            Expression<String> parentExpression = root.get(field.getName());
                            predicates.add(parentExpression.in((Object) params));
                        }
                    }
                    if (field.getType().isAssignableFrom(Integer.class)) {
                        List<Predicate> innerPredicates = new ArrayList<>();
                        for (String param : params) {
                            if (param.startsWith(">=")) {
                                String greaterThanOrEqualTo = param.substring(2);
                                int i = Integer.parseInt(greaterThanOrEqualTo);
                                innerPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(field.getName()), i));
                            }
                            if (param.startsWith("<=")) {
                                String lessThanOrEqualTo = param.substring(2);
                                int i = Integer.parseInt(lessThanOrEqualTo);
                                innerPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(field.getName()), i));
                            }
                        }
                        predicates.add(criteriaBuilder.and(innerPredicates.toArray(new Predicate[0])));
                    }
                    if (field.getType().isAssignableFrom(Date.class)) {
                        long start = Long.parseLong(params[0]);
                        long end = Long.parseLong(params[1]);
                        predicates.add(criteriaBuilder.between(root.get(field.getName()), new Date(start), new Date(end)));
                    }
                }
            }
        }

        return predicates;
    }
}

//http://lo:8080/admin/employees?page=1&size=10&sort=id&order=desc&firstName=hE%25,%25y&