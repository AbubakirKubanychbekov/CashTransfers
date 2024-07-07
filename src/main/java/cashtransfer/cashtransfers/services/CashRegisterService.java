package cashtransfer.cashtransfers.services;

import cashtransfer.cashtransfers.dto.response.PaginationResponse;
import cashtransfer.cashtransfers.entities.CashRegister;
import cashtransfer.cashtransfers.entities.User;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public interface CashRegisterService {

    void save(CashRegister cashRegister);

    List<CashRegister> findAll();
    CashRegister findById(Long cashRegisterId);

    void update(Long cashRegisterId, CashRegister newCashRegister);

    void deleteById(Long cashRegisterId);

    String transfer(Long sourceId, Long destinationId, Double amount, String currency, String senderName, String receiverName, String senderPhone, String receiverPhone, String comment);

    void confirmTransfer(String transactionId, String transferCode);

    List<CashRegister> findByCriteria(String name, Double balance);

    boolean existsByUser(User newUser);

    Health health();

    PaginationResponse getAllPagination(int currentPage, int pageSize);

    List<CashRegister> findByUser(User currentUser);
}