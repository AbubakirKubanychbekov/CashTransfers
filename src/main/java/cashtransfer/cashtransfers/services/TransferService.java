package cashtransfer.cashtransfers.services;

import cashtransfer.cashtransfers.entities.Transfer;

import java.util.List;

/**
 * @author Abubakir Dev
 */
public interface TransferService {

    List<Transfer> findAll();
}
