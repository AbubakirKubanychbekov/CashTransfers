package cashtransfer.cashtransfers.dto.response;

import lombok.Builder;

/**
 * @author Abubakir Dev
 */
@Builder
public class CashRegisterResponse {
    private Long id;
    private String name;
    private Double balance;
    private String currency;

    public CashRegisterResponse(Long id, String name, Double balance,String currency) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}