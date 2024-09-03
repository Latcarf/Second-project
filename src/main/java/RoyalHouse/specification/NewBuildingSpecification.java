package RoyalHouse.specification;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.NewBuilding;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class NewBuildingSpecification {
    public static Specification<NewBuilding> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("name"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<NewBuilding> hasAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(address)) {
                return criteriaBuilder.conjunction();
            }

            Join<NewBuilding, Address> addressJoin = root.join("address");
            return criteriaBuilder.or(
                    criteriaBuilder.like(addressJoin.get("city"), "%" + address + "%"),
                    criteriaBuilder.like(addressJoin.get("district"), "%" + address + "%")
            );
        };
    }

    public static Specification<NewBuilding> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(status) ? criteriaBuilder.like(root.get("status"), "%" + status + "%") : criteriaBuilder.conjunction();
    }
}
