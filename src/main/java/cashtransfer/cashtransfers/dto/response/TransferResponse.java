package cashtransfer.cashtransfers.dto.response;

import cashtransfer.cashtransfers.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Abubakir Dev
 */
@Builder
public class TransferResponse {
    private Long id;
    private Double amount;
    private String currency;
    private String senderName;
    private String receiverName;
    private String senderPhone;
    private String receiverPhone;
    private String comment;
    private String code;
    private Status status;
    private LocalDateTime createdAt;
    private String cashRegister;
}