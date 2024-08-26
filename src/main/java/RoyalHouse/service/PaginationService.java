package RoyalHouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface PaginationService<T> {
    Page<T> getPage(PageRequest pageRequest, Map<String, Object> filterParams);
}
