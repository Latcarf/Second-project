package RoyalHouse.dto;

import RoyalHouse.service.PaginationService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Data
@NoArgsConstructor
public class Pagination<T> {

    private int currentPage;
    private int totalPages;
    private int startPage;
    private int endPage;
    private Page<T> pageData;

    private static final Sort SORT = Sort.by(Sort.Direction.DESC, "date");
    private static final int SIZE = 5;


    public Pagination(int page, Page<T> pageData) {
        this.pageData = pageData;
        this.currentPage = page;
        this.totalPages = pageData.getTotalPages();
        this.startPage = Math.max(1, (page - 1) / 10 * 10 + 1);
        this.endPage = Math.min(startPage + 9, totalPages);
    }

    public static <T> Pagination<T> create(int page, PaginationService<T> service, Map<String, Object> filterParams) {
        PageRequest pageRequest = PageRequest.of(page - 1, SIZE, SORT);
        Page<T> pageData = service.getPage(pageRequest, filterParams);
        return new Pagination<>(page, pageData);
    }
}
