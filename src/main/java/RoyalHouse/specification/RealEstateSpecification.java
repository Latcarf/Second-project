package RoyalHouse.specification;

import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.RealEstate;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
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

    public static Specification<RealEstate> hasRoom(Integer room) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(room) || room < 1) {
                return criteriaBuilder.conjunction();
            }

            Join<RealEstate, Details> detailsJoin = root.join("details");
            return criteriaBuilder.equal(detailsJoin.get("numRooms"), room);
        };
    }

    public static Specification<RealEstate> hasCities(List<String> cities) {
        return (root, query, criteriaBuilder) ->
                root.get("address").get("city").in(cities);
    }

    public static Specification<RealEstate> hasDistricts(List<String> districts) {
        return (root, query, criteriaBuilder) ->
                root.get("address").get("district").in(districts);
    }

    public static Specification<RealEstate> builtBeforeOrAfter(int year, boolean isBefore) {
        return (root, query, criteriaBuilder) -> {
            Join<RealEstate, Details> detailsJoin = root.join("details");
            if (isBefore) {
                return criteriaBuilder.lessThan(detailsJoin.get("yearBuilt"), year);
            } else {
                return criteriaBuilder.greaterThan(detailsJoin.get("yearBuilt"), year);
            }
        };
    }

    public static Specification<RealEstate> isUnderConstruction() {
        return (root, query, criteriaBuilder) -> {
            Join<RealEstate, Details> detailsJoin = root.join("details");
            return criteriaBuilder.greaterThan(detailsJoin.get("yearBuilt"), LocalDateTime.now().getYear());
        };
    }

}
