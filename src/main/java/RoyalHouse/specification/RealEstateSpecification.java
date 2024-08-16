package RoyalHouse.specification;

import RoyalHouse.model.Building.RealEstate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class RealEstateSpecification {

    public static Specification<RealEstate> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("name"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<RealEstate> hasType(String type) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(type) ? criteriaBuilder.like(root.get("type"), "%" + type + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<RealEstate> hasRoom(int room) {
        return (root, query, criteriaBuilder) ->
                room == 0 ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("numRooms"), room);
    }
}
