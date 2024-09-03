package RoyalHouse.specification;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class RealEstateSpecification {

    public static Specification<RealEstate> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("name"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<RealEstate> hasType(String type) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(type) ? criteriaBuilder.like(root.get("type"), "%" + type + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<RealEstate> hasRoom(Integer room) {
        return (root, query, criteriaBuilder) -> {
            if (room == 0) {
                return criteriaBuilder.conjunction();
            }

            Join<RealEstate, Details> detailsJoin = root.join("details");
            return criteriaBuilder.equal(detailsJoin.get("numRooms"), room);
        };
    }


}
