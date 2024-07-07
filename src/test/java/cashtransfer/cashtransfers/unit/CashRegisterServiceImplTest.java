package cashtransfer.cashtransfers.unit;

import cashtransfer.cashtransfers.entities.CashRegister;
import cashtransfer.cashtransfers.entities.Transfer;
import cashtransfer.cashtransfers.repositories.CashRegisterRepository;
import cashtransfer.cashtransfers.repositories.TransferRepository;
import cashtransfer.cashtransfers.services.impl.CashRegisterImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Abubakir Dev
 */
@ExtendWith(MockitoExtension.class)
public class CashRegisterServiceImplTest {

    @Mock
    private CashRegisterRepository cashRegisterRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private CashRegisterImpl cashRegisterService;

    @Test
    public void testSuccessfulTransfer() {
        Long sourceId = 1L;
        Long destinationId = 2L;
        Double amount = 100.0;
        String currency = "USD";
        String senderName = "Sender";
        String receiverName = "Receiver";
        String senderPhone = "123456789";
        String receiverPhone = "987654321";
        String comment = "Test transfer";

        CashRegister source = new CashRegister();
        source.setId(sourceId);
        source.setBalance(500.0);

        CashRegister destination = new CashRegister();
        destination.setId(destinationId);
        destination.setBalance(200.0);

        when(cashRegisterRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(cashRegisterRepository.findById(destinationId)).thenReturn(Optional.of(destination));

        String confirmationCode = cashRegisterService.transfer(sourceId, destinationId, amount, currency,
                senderName, receiverName, senderPhone, receiverPhone, comment);

        assertEquals(400.0, source.getBalance(), "Source balance should be decreased by 100");
        assertEquals(300.0, destination.getBalance(), "Destination balance should be increased by 100");

        verify(transferRepository, times(1)).save(any(Transfer.class));
        assertNotNull(confirmationCode, "Confirmation code should not be null");
    }

    @Test
    public void testTransferToSameAccount() {
        Long accountId = 1L;
        Double amount = 100.0;
        String currency = "USD";
        String senderName = "Sender";
        String receiverName = "Receiver";
        String senderPhone = "123456789";
        String receiverPhone = "987654321";
        String comment = "Test transfer";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashRegisterService.transfer(accountId, accountId, amount, currency, senderName, receiverName,
                    senderPhone, receiverPhone, comment);
        });

        assertEquals("Transfer to the same account is not allowed", exception.getMessage(),
                "Exception message should match");
    }

    @Test
    public void testTransferSourceNotFound() {
        Long sourceId = 1L;
        Long destinationId = 2L;
        Double amount = 100.0;
        String currency = "USD";
        String senderName = "Sender";
        String receiverName = "Receiver";
        String senderPhone = "123456789";
        String receiverPhone = "987654321";
        String comment = "Test transfer";

        when(cashRegisterRepository.findById(sourceId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cashRegisterService.transfer(sourceId, destinationId, amount, currency, senderName, receiverName,
                    senderPhone, receiverPhone, comment);
        });

        assertEquals("Source not found", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testTransferDestinationNotFound() {
        Long sourceId = 1L;
        Long destinationId = 2L;
        Double amount = 100.0;
        String currency = "USD";
        String senderName = "Sender";
        String receiverName = "Receiver";
        String senderPhone = "123456789";
        String receiverPhone = "987654321";
        String comment = "Test transfer";

        CashRegister source = new CashRegister();
        source.setId(sourceId);
        source.setBalance(500.0);

        when(cashRegisterRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(cashRegisterRepository.findById(destinationId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cashRegisterService.transfer(sourceId, destinationId, amount, currency, senderName, receiverName,
                    senderPhone, receiverPhone, comment);
        });

        assertEquals("Destination not found", exception.getMessage(), "Exception message should match");
    }


    @Test
    public void testSaveCashRegister() {
        CashRegister cashRegister = new CashRegister();
        cashRegister.setName("Test Cash Register");
        cashRegister.setBalance(100.0);

        when(cashRegisterRepository.save(any(CashRegister.class))).thenReturn(cashRegister);
        cashRegisterService.save(cashRegister);
        verify(cashRegisterRepository, times(1)).save(cashRegister);
    }

    @Test
    public void testFindAllCashRegisters() {
        CashRegister cashRegister1 = new CashRegister();
        cashRegister1.setId(1L);
        cashRegister1.setName("Cash Register 1");
        cashRegister1.setBalance(200.0);

        CashRegister cashRegister2 = new CashRegister();
        cashRegister2.setId(2L);
        cashRegister2.setName("Cash Register 2");
        cashRegister2.setBalance(300.0);

        List<CashRegister> cashRegisterList = Arrays.asList(cashRegister1, cashRegister2);
        when(cashRegisterRepository.findAll()).thenReturn(cashRegisterList);
        List<CashRegister> retrievedCashRegisters = cashRegisterService.findAll();

        assertEquals(2, retrievedCashRegisters.size(), "Should retrieve 2 cash registers");
        assertEquals("Cash Register 1", retrievedCashRegisters.get(0).getName(), "First cash register name should match");
        assertEquals("Cash Register 2", retrievedCashRegisters.get(1).getName(), "Second cash register name should match");
    }

    @Test
    public void testFindByIdCashRegister() {
        Long cashRegisterId = 1L;
        CashRegister cashRegister = new CashRegister();
        cashRegister.setId(cashRegisterId);
        cashRegister.setName("Test Cash Register");
        cashRegister.setBalance(100.0);

        when(cashRegisterRepository.findById(cashRegisterId)).thenReturn(Optional.of(cashRegister));

        CashRegister retrievedCashRegister = cashRegisterService.findById(cashRegisterId);
        assertEquals("Test Cash Register", retrievedCashRegister.getName(), "Cash register name should match");
    }

    @Test
    public void testUpdateCashRegister() {
        Long cashRegisterId = 1L;
        CashRegister existingCashRegister = new CashRegister();
        existingCashRegister.setId(cashRegisterId);
        existingCashRegister.setName("Existing Cash Register");
        existingCashRegister.setBalance(200.0);

        CashRegister updatedCashRegister = new CashRegister();
        updatedCashRegister.setName("Updated Cash Register");
        updatedCashRegister.setBalance(300.0);

        when(cashRegisterRepository.findById(cashRegisterId)).thenReturn(Optional.of(existingCashRegister));
        when(cashRegisterRepository.save(any(CashRegister.class))).thenReturn(updatedCashRegister);
        cashRegisterService.update(cashRegisterId, updatedCashRegister);

        assertEquals("Updated Cash Register", existingCashRegister.getName(), "Cash register name should be updated");
        assertEquals(300.0, existingCashRegister.getBalance(), 0.001, "Cash register balance should be updated");
    }

    @Test
    public void testDeleteByIdCashRegister() {
        Long cashRegisterId = 1L;

        cashRegisterService.deleteById(cashRegisterId);
        verify(cashRegisterRepository, times(1)).deleteById(cashRegisterId);
    }
}
