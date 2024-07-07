package cashtransfer.cashtransfers.dto.request;

import cashtransfer.cashtransfers.enums.Status;
import lombok.Builder;

/**
 * @author Abubakir Dev
 */
@Builder
public record TransferRequest(
        Long cashRegisterId,
        Double amount,
        String currency,
        String senderName,
        String receiverName,
        String senderPhone,
        String receiverPhone,
        String comment,
        Status status
) {
}