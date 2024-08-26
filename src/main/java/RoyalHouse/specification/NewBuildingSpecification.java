package RoyalHouse.specification;

import RoyalHouse.model.building.NewBuilding;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class NewBuildingSpecification {
    public static Specification<NewBuilding> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("name"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<NewBuilding> hasAddress(String address) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(address) ? criteriaBuilder.like(root.get("type"), "%" + address + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<NewBuilding> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(status) ? criteriaBuilder.like(root.get("status"), "%" + status + "%") : criteriaBuilder.conjunction();
    }
}
