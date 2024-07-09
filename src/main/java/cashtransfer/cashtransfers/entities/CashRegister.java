package cashtransfer.cashtransfers.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Entity
@Table(name = "cash_registers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double balance;
    private String urlImage;
    @Column(nullable = false)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "source")
    private List<Transfer> outgoingTransfers;

    @OneToMany(mappedBy = "destination")
    private List<Transfer> incomingTransfers;

    public CashRegister(Long id, String name, Double balance, String urlImage, String currency) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.urlImage = urlImage;
        this.currency = currency;
    }
}