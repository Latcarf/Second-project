package RoyalHouse.specification;

import RoyalHouse.model.Request;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RequestSpecification {

    public static Specification<Request> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("userName"), "%" + name + "%");
    }

    public static Specification<Request> hasPhone(String phone) {
        return (root, query, criteriaBuilder) ->
                phone == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<Request> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Request> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                date == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("date").as(LocalDate.class), date);
    }

    public static Specification<Request> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }
}
