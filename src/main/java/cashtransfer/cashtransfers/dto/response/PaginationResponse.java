package cashtransfer.cashtransfers.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Builder
public class PaginationResponse <T>{
    private List<T> t;
    private int currentPage;
    private int pageSize;
    private int totalElements;

    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    public List<T> getCashRegisters() {
        return t;
    }
}
