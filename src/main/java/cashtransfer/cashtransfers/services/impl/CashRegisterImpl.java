package cashtransfer.cashtransfers.services.impl;

import cashtransfer.cashtransfers.entities.CashRegister;
import cashtransfer.cashtransfers.entities.Transfer;
import cashtransfer.cashtransfers.entities.User;
import cashtransfer.cashtransfers.enums.Role;
import cashtransfer.cashtransfers.enums.Status;
import cashtransfer.cashtransfers.exceptions.NotFoundException;
import cashtransfer.cashtransfers.repositories.CashRegisterRepository;
import cashtransfer.cashtransfers.repositories.TransferRepository;
import cashtransfer.cashtransfers.repositories.UserRepository;
import cashtransfer.cashtransfers.services.CashRegisterService;
import cashtransfer.cashtransfers.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Abubakir Dev
 */
@Service
@RequiredArgsConstructor
@Transactional
@Component
@Slf4j
public class CashRegisterImpl implements CashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final UserService userService;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Здесь проверяем приложение с Actuator ом
     */
    @Override
    public Health health() {
        boolean healthCheckResult = true;

        if (healthCheckResult) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("Error", "Something went wrong").build(); // Приложение не работает
        }
    }

    @Transactional
    @Override
    public String transfer(Long sourceId, Long destinationId, Double amount, String currency, String senderName, String receiverName, String senderPhone, String receiverPhone, String comment) {
        // Проверка, чтобы пользователь сам себе не отправиль
        if (sourceId.equals(destinationId)) {
            throw new IllegalArgumentException("Transfer to the same account is not allowed");
        }

        // Получение учетных записей отправителя и получателя
        CashRegister source = cashRegisterRepository.findById(sourceId).orElseThrow(() -> new RuntimeException("Source not found"));
        CashRegister destination = cashRegisterRepository.findById(destinationId).orElseThrow(() -> new RuntimeException("Destination not found"));

        // Выполнить перевод
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        cashRegisterRepository.save(source);
        cashRegisterRepository.save(destination);

        // Создание записи о переводе
        Transfer transfer = new Transfer();
        transfer.setSource(source);
        transfer.setDestination(destination);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());
        transfer.setConfirmationCode(generateTransferCode());
        transfer.setCurrency(currency);
        transfer.setSenderName(senderName);
        transfer.setReceiverName(receiverName);
        transfer.setSenderPhone(senderPhone);
        transfer.setReceiverPhone(receiverPhone);
        transfer.setComment(comment);
        transfer.setStatus(Status.PENDING);
        transferRepository.save(transfer);
        log.info("Transfer successfully saved");
        return transfer.getConfirmationCode();
    }

    /**
     * Логика подтверждения перевода
     */
    @Override
    public void confirmTransfer(String transactionId, String transferCode) {
        Transfer transfer = transferRepository.findByConfirmationCode(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!transfer.getConfirmationCode().equals(transferCode)) {
            throw new RuntimeException("Invalid transfer code");
        }

        transfer.setStatus(Status.COMPLETED);
        transferRepository.save(transfer);
    }

    /**
     * Генерация кода для подтверждения перевода
     */
    private String generateTransferCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public void save(CashRegister cashRegister) {
        cashRegisterRepository.save(cashRegister);
        log.info("Cash register success saved");
    }

    @Override
    public List<CashRegister> findAll() {
        return cashRegisterRepository.findAll();
    }

    @Override
    public CashRegister findById(Long cashRegisterId) {
        return cashRegisterRepository.findById(cashRegisterId).orElseThrow(()->
                new NotFoundException("Cash Register by id "+ cashRegisterId + " not found"));
    }

    @Override
    public void update(Long cashRegisterId, CashRegister newCashRegister) {
       CashRegister cashRegister = cashRegisterRepository.findById(cashRegisterId).orElseThrow(()->
               new NotFoundException("Cash Register by id "+ cashRegisterId + " not found"));
       cashRegister.setName(newCashRegister.getName());
       cashRegister.setBalance(newCashRegister.getBalance());
       cashRegisterRepository.save(cashRegister);
       log.info("Cash register with id " + cashRegisterId + " success updated");
    }

    @Override
    public void deleteById(Long cashRegisterId) {
        cashRegisterRepository.deleteById(cashRegisterId);
        log.info("Cash register with id " + cashRegisterId + " success deleted");
    }

    @Override
    public boolean existsByUser(User newUser) {
        return cashRegisterRepository.existsByUser(newUser);
    }


    @Override
    public List<CashRegister> findByCriteria(String name, Double balance) {
        return cashRegisterRepository.findByCriteria(name, balance);
    }


    @PostConstruct
    public void initMethod(){
        User u = User.builder()
                .firstName("Abubakir")
                .lastName("Kubanychbekov")
                .email("abubakir@gmail.com")
                .password(passwordEncoder.encode("abubakir"))
                .role(Role.OWNER_CARD)
                .build();
        userRepository.save(u);
    }
}
