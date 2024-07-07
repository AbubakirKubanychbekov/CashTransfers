package cashtransfer.cashtransfers.entities;

import cashtransfer.cashtransfers.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Abubakir Dev
 */
@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private CashRegister source;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private CashRegister destination;

    private Double amount;
    private LocalDateTime timestamp;
    private String confirmationCode; // Новый код подтверждения

    private String currency;
    private String senderName;
    private String receiverName;
    private String senderPhone;
    private String receiverPhone;
    private String comment;
    @Enumerated(EnumType.STRING)
    private Status status;
}