package cashtransfer.cashtransfers.services.impl;

import cashtransfer.cashtransfers.entities.Transfer;
import cashtransfer.cashtransfers.repositories.CashRegisterRepository;
import cashtransfer.cashtransfers.repositories.TransferRepository;
import cashtransfer.cashtransfers.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CashRegisterRepository cashRegisterRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }
}