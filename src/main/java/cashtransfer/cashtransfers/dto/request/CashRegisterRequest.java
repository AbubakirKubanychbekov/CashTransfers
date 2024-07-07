package cashtransfer.cashtransfers.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Abubakir Dev
 */
@Builder
@Getter @Setter
public class CashRegisterRequest {
    private String name;
    private Double balance;
    private String urlImage;
}
