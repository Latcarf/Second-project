package RoyalHouse.specification;

import RoyalHouse.model.company.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ServiceSpecification {
    public static Specification<Service> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("name"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<Service> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(status) ? criteriaBuilder.like(root.get("status"), "%" + status + "%") : criteriaBuilder.conjunction();
    }
}
