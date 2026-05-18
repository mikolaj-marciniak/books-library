package pl.edu.pwr.ztw.books.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.edu.pwr.ztw.books.dto.PaginatedResponse;

public final class PaginationUtils {
    public static final int PAGE_SIZE = 5;

    private PaginationUtils() {
    }

    public static <T> PaginatedResponse<T> paginate(Collection<T> items, int page) {
        List<T> itemList = new ArrayList<>(items);
        int safePage = Math.max(page, 0);
        int totalElements = itemList.size();
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / PAGE_SIZE);

        int fromIndex = safePage * PAGE_SIZE;
        if (fromIndex >= totalElements) {
            return new PaginatedResponse<>(Collections.emptyList(), safePage, PAGE_SIZE, totalElements, totalPages);
        }

        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalElements);
        List<T> pageContent = itemList.subList(fromIndex, toIndex);

        return new PaginatedResponse<>(pageContent, safePage, PAGE_SIZE, totalElements, totalPages);
    }
}
