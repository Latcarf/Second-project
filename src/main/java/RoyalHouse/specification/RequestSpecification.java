package RoyalHouse.specification;

import RoyalHouse.model.Request;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

public class RequestSpecification {

    public static Specification<Request> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(name) ? criteriaBuilder.like(root.get("userName"), "%" + name + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<Request> hasPhone(String phone) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(phone) ? criteriaBuilder.like(root.get("phone"), "%" + phone + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<Request> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(email) ? criteriaBuilder.like(root.get("email"), "%" + email + "%") : criteriaBuilder.conjunction();
    }

    public static Specification<Request> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                Objects.isNull(date) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("date").as(LocalDate.class), date);
    }

    public static Specification<Request> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(status) ? criteriaBuilder.equal(root.get("status"), status) : criteriaBuilder.conjunction();
    }
}
